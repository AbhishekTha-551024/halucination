package com.master.rummy.ui.table

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.master.rummy.core.engine.AutoGrouper
import com.master.rummy.core.engine.BotPlayer
import com.master.rummy.core.engine.Card
import com.master.rummy.core.engine.GroupType
import com.master.rummy.core.engine.Rank
import com.master.rummy.core.engine.RummyValidator
import com.master.rummy.core.engine.Suit
import com.master.rummy.ui.components.PlayingCardView
import com.master.rummy.ui.lobby.RoomTier
import com.master.rummy.ui.theme.AutoSortBlue
import com.master.rummy.ui.theme.DropButtonRed
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.LaurelGold
import com.master.rummy.ui.theme.MaroonHeader
import com.master.rummy.ui.theme.ScoreHeaderMaroon
import com.master.rummy.ui.theme.StatusGreen
import com.master.rummy.ui.theme.StatusRed
import com.master.rummy.ui.theme.TableBorderBrown
import com.master.rummy.ui.theme.TableFeltGreen
import com.master.rummy.ui.theme.TableWallNavy
import com.master.rummy.ui.theme.WinnerRowGold
import kotlinx.coroutines.delay

enum class TurnState {
    NEED_PICK,
    NEED_DISCARD,
    OPPONENT_TURN,
    GAME_OVER
}

@Composable
fun GameTableScreen(
    tier: RoomTier,
    userName: String,
    onLeaveTable: () -> Unit
) {
    // Dynamic Landscape Orientation Lock
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    // Wild Joker setup (10 of Hearts as in live device screenshot)
    val wildJokerCard = remember { Card(999, Suit.HEARTS, Rank.TEN, isWildJoker = true) }

    fun generateDeck(): MutableList<Card> {
        val list = mutableListOf<Card>()
        var id = 1
        for (suit in Suit.entries) {
            for (rank in Rank.entries) {
                val isWild = rank == wildJokerCard.rank
                list.add(Card(id++, suit, rank, isWildJoker = isWild))
            }
        }
        list.shuffle()
        return list
    }

    val fullDeck = remember { generateDeck() }

    // Initial 13 cards hand
    val initialRawHand = remember {
        val deal = fullDeck.take(13).toMutableList()
        repeat(13) { fullDeck.removeAt(0) }
        deal
    }

    var cardGroups by remember {
        mutableStateOf(AutoGrouper.autoGroup(initialRawHand))
    }

    val bot = remember {
        BotPlayer().apply {
            hand = fullDeck.take(13).toMutableList()
            repeat(13) { fullDeck.removeAt(0) }
        }
    }

    var openDiscardCard by remember {
        mutableStateOf(Card(888, Suit.HEARTS, Rank.NINE))
    }

    var selectedCards by remember { mutableStateOf(setOf<Card>()) }
    var turnState by remember { mutableStateOf(TurnState.NEED_PICK) }
    var turnTimeRemaining by remember { mutableIntStateOf(30) }
    var showScoreWindow by remember { mutableStateOf(false) }
    var finishSlotOccupied by remember { mutableStateOf(false) }
    var showTooltip by remember { mutableStateOf(true) }

    // Table Dialogs
    var showTableMenuDialog by remember { mutableStateOf(false) }
    var showChatDialog by remember { mutableStateOf(false) }
    var activeChatMessage by remember { mutableStateOf<String?>(null) }
    var soundEnabled by remember { mutableStateOf(true) }

    // Message timer
    LaunchedEffect(activeChatMessage) {
        if (activeChatMessage != null) {
            delay(3500)
            activeChatMessage = null
        }
    }

    // Flattened player hand
    val flatHand = cardGroups.flatten()

    // Real-time calculation of player's score
    val currentPenaltyScore = remember(cardGroups) {
        val declaration = RummyValidator.validateDeclaration(cardGroups)
        declaration.penaltyPoints
    }

    // Turn timer countdown
    LaunchedEffect(turnState, turnTimeRemaining) {
        if (turnState != TurnState.GAME_OVER && turnTimeRemaining > 0) {
            delay(1000)
            turnTimeRemaining -= 1
        } else if (turnTimeRemaining == 0) {
            if (turnState == TurnState.NEED_PICK) {
                if (fullDeck.isNotEmpty()) {
                    val drawn = fullDeck.removeAt(0)
                    cardGroups = cardGroups.mapIndexed { idx, grp ->
                        if (idx == cardGroups.lastIndex) grp + drawn else grp
                    }
                    turnState = TurnState.NEED_DISCARD
                    turnTimeRemaining = 16
                    showTooltip = false
                }
            } else if (turnState == TurnState.NEED_DISCARD) {
                val cardToDiscard = selectedCards.firstOrNull() ?: flatHand.lastOrNull()
                cardToDiscard?.let { discarded ->
                    cardGroups = cardGroups.map { grp -> grp.filter { it.id != discarded.id } }.filter { it.isNotEmpty() }
                    openDiscardCard = discarded
                    selectedCards = emptySet()
                    turnState = TurnState.OPPONENT_TURN
                    turnTimeRemaining = 30
                }
            }
        }
    }

    // Opponent AI turn
    LaunchedEffect(turnState) {
        if (turnState == TurnState.OPPONENT_TURN) {
            delay(2200)
            val (discarded, botWon) = bot.takeTurn(openDiscardCard) {
                if (fullDeck.isNotEmpty()) fullDeck.removeAt(0) else openDiscardCard
            }
            openDiscardCard = discarded

            if (botWon) {
                showScoreWindow = true
                turnState = TurnState.GAME_OVER
            } else {
                turnState = TurnState.NEED_PICK
                turnTimeRemaining = 30
                showTooltip = true
            }
        }
    }

    // ==========================================
    // ROOT LANDSCAPE CONTAINER
    // ==========================================
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TableWallNavy) // Navy wallpaper border
    ) {
        // Oval Green Felt Poker Table
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(percent = 45))
                .background(TableBorderBrown) // Padded leather rail
                .border(2.dp, Color(0xFF5D4037), RoundedCornerShape(percent = 45))
                .padding(8.dp) // Thickness of leather rail
                .clip(RoundedCornerShape(percent = 45))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF138A4B), TableFeltGreen, Color(0xFF074E28)),
                        radius = 900f
                    )
                )
        ) {
            // Main Content Inside Table
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // ------------------------------------------
                // 1. TOP HEADER BAR
                // ------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Menu Hamburger & Score Log Button
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .clickable { showTableMenuDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("☰", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .clickable { showScoreWindow = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📋", fontSize = 14.sp)
                        }
                    }

                    // Center: Translucent Pill (Shield + Table ID + Mode + Signal)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🛡️", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "1416826090",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Points Rummy",
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("|", color = Color.Gray, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Value: 🪙 ${tier.pointValue}",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("📶", fontSize = 11.sp)
                        }
                    }

                    // Right: Refresh / Switch Table Button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.55f))
                            .clickable {
                                cardGroups = AutoGrouper.autoGroup(fullDeck.take(13))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔄", fontSize = 14.sp)
                    }
                }

                // ------------------------------------------
                // 2. OPPONENT SEAT (Top Center)
                // ------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.TopStart) {
                        // Avatar Circle
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.5.dp, GoldAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👨‍💼", fontSize = 26.sp)
                        }

                        // Yellow Dealer Button "D"
                        Box(
                            modifier = Modifier
                                .offset(x = (-4).dp, y = (-2).dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD600))
                                .border(0.5.dp, Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("D", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    // Name Tag
                    Box(
                        modifier = Modifier
                            .offset(y = (-4).dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = bot.name,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // ------------------------------------------
                // 3. CENTER TABLE: DECKS + FINISH SLOT + TOOLTIP
                // ------------------------------------------
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Floating Tooltip above decks
                    if (showTooltip && turnState == TurnState.NEED_PICK) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-46).dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.85f))
                                .border(1.dp, GoldAccent.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Pick from CLOSED or OPEN deck",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        // A. CLOSED DRAW PILE (With Wild Joker underneath)
                        Box(contentAlignment = Alignment.Center) {
                            // Yellow Pointer Indicator
                            if (turnState == TurnState.NEED_PICK) {
                                Text(
                                    text = "▼",
                                    color = Color(0xFFFFEB3B),
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .offset(y = (-14).dp)
                                )
                            }

                            // Wild Joker peeking horizontally under the deck
                            PlayingCardView(
                                card = wildJokerCard,
                                isFaceUp = true,
                                width = 36.dp,
                                height = 54.dp,
                                modifier = Modifier.offset(x = (-24).dp)
                            )

                            // Top Closed Card (Gold Card Back)
                            Box(
                                modifier = Modifier.clickable(enabled = turnState == TurnState.NEED_PICK) {
                                    if (fullDeck.isNotEmpty()) {
                                        val drawn = fullDeck.removeAt(0)
                                        cardGroups = cardGroups.mapIndexed { idx, grp ->
                                            if (idx == cardGroups.lastIndex) grp + drawn else grp
                                        }
                                        turnState = TurnState.NEED_DISCARD
                                        showTooltip = false
                                    }
                                }
                            ) {
                                PlayingCardView(
                                    card = Card(0, Suit.SPADES, Rank.ACE),
                                    isFaceUp = false,
                                    width = 44.dp,
                                    height = 64.dp
                                )
                            }
                        }

                        // B. FINISH SLOT
                        Box(
                            modifier = Modifier
                                .size(width = 46.dp, height = 64.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF094E27).copy(alpha = 0.85f))
                                .border(
                                    width = 1.dp,
                                    color = if (finishSlotOccupied) Color(0xFF69F0AE) else Color(0xFF1B5E20),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    if (turnState == TurnState.NEED_DISCARD && selectedCards.isNotEmpty()) {
                                        finishSlotOccupied = true
                                        showScoreWindow = true
                                        turnState = TurnState.GAME_OVER
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "FINISH",
                                    color = Color(0xFF69F0AE),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "SLOT",
                                    color = Color(0xFF69F0AE),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // C. OPEN DISCARD PILE (With Fast Pick button >>)
                        Box(contentAlignment = Alignment.Center) {
                            // Yellow Pointer Indicator
                            if (turnState == TurnState.NEED_PICK) {
                                Text(
                                    text = "▼",
                                    color = Color(0xFFFFEB3B),
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .offset(y = (-14).dp)
                                )
                            }

                            // Open Card
                            PlayingCardView(
                                card = openDiscardCard,
                                isFaceUp = true,
                                width = 44.dp,
                                height = 64.dp,
                                onClick = {
                                    if (turnState == TurnState.NEED_PICK) {
                                        cardGroups = cardGroups.mapIndexed { idx, grp ->
                                            if (idx == cardGroups.lastIndex) grp + openDiscardCard else grp
                                        }
                                        openDiscardCard = Card(777, Suit.CLUBS, Rank.FIVE)
                                        turnState = TurnState.NEED_DISCARD
                                        showTooltip = false
                                    }
                                }
                            )

                            // Blue Fast Discard/Draw Button ">>"
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .offset(x = 18.dp)
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF0288D1))
                                    .clickable {
                                        if (turnState == TurnState.NEED_DISCARD && selectedCards.isNotEmpty()) {
                                            val card = selectedCards.first()
                                            cardGroups = cardGroups.map { grp -> grp.filter { it.id != card.id } }.filter { it.isNotEmpty() }
                                            openDiscardCard = card
                                            selectedCards = emptySet()
                                            turnState = TurnState.OPPONENT_TURN
                                            turnTimeRemaining = 30
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("»", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Rating Indicator & Chat Button on Right
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("★ 3.0", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .clickable { showChatDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💬", fontSize = 14.sp)
                        }
                    }
                }

                // ------------------------------------------
                // 4. PLAYER HAND (MELD GROUPS WITH STATUS PILLS & ANIMATION)
                // ------------------------------------------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    cardGroups.forEachIndexed { groupIndex, groupCards ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            // Overlapping Cards Row within this group
                            Box(
                                modifier = Modifier
                                    .height(78.dp)
                                    .width((44 + (groupCards.size - 1) * 22).dp)
                            ) {
                                groupCards.forEachIndexed { cardIndex, card ->
                                    val isSelected = selectedCards.contains(card)
                                    val cardOffsetY by animateDpAsState(
                                        targetValue = if (isSelected) (-14).dp else 0.dp,
                                        animationSpec = tween(durationMillis = 150),
                                        label = "cardElevation"
                                    )

                                    PlayingCardView(
                                        card = card,
                                        isSelected = isSelected,
                                        isFaceUp = true,
                                        width = 44.dp,
                                        height = 64.dp,
                                        modifier = Modifier
                                            .offset(x = (cardIndex * 22).dp, y = cardOffsetY)
                                            .align(Alignment.BottomStart),
                                        onClick = {
                                            selectedCards = if (isSelected) {
                                                selectedCards - card
                                            } else {
                                                selectedCards + card
                                            }
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Group Classification & Status Badge Pill
                            val classified = remember(groupCards) { RummyValidator.classifyGroup(groupCards) }
                            GroupStatusPill(type = classified.type, points = classified.points)
                        }
                    }
                }

                // ------------------------------------------
                // 5. BOTTOM ACTION BAR (Exact Buttons from Screenshot)
                // ------------------------------------------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Points & Plus Pill
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF007E33))
                                .border(1.dp, Color(0xFF00C851), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Points", color = Color.White, fontSize = 8.sp)
                                Text("🪙 ${tier.pointValue}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.White, fontSize = 14.sp)
                        }
                    }

                    // Chips Balance Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🪙", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("6349", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Center: User Avatar with Dual Countdown Timer Ring & Chat Bubble
                    Box(contentAlignment = Alignment.TopCenter) {
                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .border(2.dp, Color(0xFF00E676), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧢", fontSize = 24.sp)
                        }

                        // Digital Timer Badge over avatar
                        Box(
                            modifier = Modifier
                                .offset(y = (-6).dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF00C851))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$turnTimeRemaining",
                                color = Color.Black,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Active Chat Bubble popup over avatar
                        activeChatMessage?.let { msg ->
                            Box(
                                modifier = Modifier
                                    .offset(y = (-36).dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, GoldAccent, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(msg, color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Score Display
                    Text(
                        text = "Score: $currentPenaltyScore",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Contextual Action Buttons: GROUP, DISCARD, DROP, AUTO SORT
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // If multiple cards are selected, show GROUP button
                        if (selectedCards.size >= 2) {
                            Button(
                                onClick = {
                                    val newGroup = selectedCards.toList()
                                    val remaining = cardGroups.map { grp -> grp.filter { !selectedCards.contains(it) } }.filter { it.isNotEmpty() }
                                    cardGroups = remaining + listOf(newGroup)
                                    selectedCards = emptySet()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("GROUP", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        // If in NEED_DISCARD and card selected, show DISCARD button
                        if (turnState == TurnState.NEED_DISCARD && selectedCards.isNotEmpty()) {
                            Button(
                                onClick = {
                                    val card = selectedCards.first()
                                    cardGroups = cardGroups.map { grp -> grp.filter { it.id != card.id } }.filter { it.isNotEmpty() }
                                    openDiscardCard = card
                                    selectedCards = emptySet()
                                    turnState = TurnState.OPPONENT_TURN
                                    turnTimeRemaining = 30
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("DISCARD", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        // DROP Button (Red with penalty points)
                        Button(
                            onClick = {
                                showScoreWindow = true
                                turnState = TurnState.GAME_OVER
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DropButtonRed),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("DROP", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                Text("1000", color = Color.White.copy(alpha = 0.9f), fontSize = 8.sp)
                            }
                        }

                        // AUTO SORT Button (Blue with checkmark)
                        Button(
                            onClick = {
                                val allCards = cardGroups.flatten()
                                cardGroups = AutoGrouper.autoGroup(allCards)
                                selectedCards = emptySet()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AutoSortBlue),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✔", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("AUTO\nSORT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 9.sp, lineHeight = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 6. IN-GAME MENU DIALOG
        // ==========================================
        if (showTableMenuDialog) {
            Dialog(onDismissRequest = { showTableMenuDialog = false }) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.55f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("TABLE MENU", fontWeight = FontWeight.Black, fontSize = 14.sp, color = MaroonHeader)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Game Audio / Sound FX", fontSize = 12.sp)
                            Switch(
                                checked = soundEnabled,
                                onCheckedChange = { soundEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF00A32A))
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF0F0F0))

                        Button(
                            onClick = {
                                showTableMenuDialog = false
                                cardGroups = AutoGrouper.autoGroup(fullDeck.take(13))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Text("Switch Table 🔄", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                showTableMenuDialog = false
                                onLeaveTable()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Text("Leave Table (Exit to Lobby)", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // ==========================================
        // 7. IN-GAME QUICK CHAT DIALOG
        // ==========================================
        if (showChatDialog) {
            Dialog(onDismissRequest = { showChatDialog = false }) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("QUICK CHAT", fontWeight = FontWeight.Black, fontSize = 13.sp, color = MaroonHeader)
                        Spacer(modifier = Modifier.height(8.dp))

                        listOf(
                            "Good luck! 🍀",
                            "Well played! 👏",
                            "Nice move! ⭐",
                            "Nice Joker! 🃏",
                            "Hurry up! ⏰",
                            "Rematch? ⚔️"
                        ).forEach { msg ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activeChatMessage = msg
                                        showChatDialog = false
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                            ) {
                                Text(msg, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                            HorizontalDivider(color = Color(0xFFF5F5F5))
                        }
                    }
                }
            }
        }

        // ==========================================
        // 8. SCORE WINDOW POPUP (Exact Settlement Dialog)
        // ==========================================
        if (showScoreWindow) {
            ScoreWindowDialog(
                userName = userName,
                opponentName = bot.name,
                userPenaltyScore = currentPenaltyScore,
                onExit = onLeaveTable,
                onStartAgain = {
                    showScoreWindow = false
                    fullDeck.clear()
                    fullDeck.addAll(generateDeck())
                    cardGroups = AutoGrouper.autoGroup(fullDeck.take(13))
                    repeat(13) { fullDeck.removeAt(0) }
                    turnState = TurnState.NEED_PICK
                    turnTimeRemaining = 30
                    finishSlotOccupied = false
                }
            )
        }
    }
}

@Composable
fun GroupStatusPill(type: GroupType, points: Int) {
    val (text, bgColor) = when (type) {
        GroupType.PURE_SEQUENCE -> Pair("✔ Pure Sequence", StatusGreen)
        GroupType.IMPURE_SEQUENCE -> Pair("✔ Sequence", Color(0xFFF57C00))
        GroupType.SET -> Pair("✔ Set", Color(0xFF1976D2))
        GroupType.INVALID -> Pair("✖ Invalid($points)", StatusRed)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ScoreWindowDialog(
    userName: String,
    opponentName: String,
    userPenaltyScore: Int,
    onExit: () -> Unit,
    onStartAgain: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.82f),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar (Deep Maroon)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ScoreHeaderMaroon)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SCORE WINDOW",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp
                    )
                }

                // Column Titles: Result, Cards, Score, Won
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Result", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
                    Text("Cards", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2.5f))
                    Text("Score", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("Won", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                }

                // Row 1: Winner Opponent Row (Pale Gold Background)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WinnerRowGold)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Avatar & Winner Laurel
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, LaurelGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👨‍💼", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("👑 WINNER", color = LaurelGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                Text(opponentName.take(8), color = Color.DarkGray, fontSize = 9.sp)
                            }
                        }

                        // 13 Mini Gold Cards
                        Row(
                            modifier = Modifier.weight(2.5f),
                            horizontalArrangement = Arrangement.spacedBy((-10).dp)
                        ) {
                            repeat(13) {
                                PlayingCardView(
                                    card = Card(it, Suit.SPADES, Rank.ACE),
                                    isFaceUp = false,
                                    width = 18.dp,
                                    height = 26.dp
                                )
                            }
                        }

                        // Score 0
                        Text("0", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                        // Won +873
                        Text("873", color = Color(0xFF2E7D32), fontSize = 14.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                    }
                }

                // Row 2: User Dropped / Lost Row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // User Avatar with ME red ribbon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Box(contentAlignment = Alignment.TopStart) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1E293B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🧢", fontSize = 20.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .offset(x = (-4).dp, y = (-2).dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color.Red)
                                        .padding(horizontal = 2.dp)
                                ) {
                                    Text("ME", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("DROPPED", color = Color.DarkGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(userName.take(8), color = Color.Gray, fontSize = 9.sp)
                            }
                        }

                        // 13 Mini Gold Cards
                        Row(
                            modifier = Modifier.weight(2.5f),
                            horizontalArrangement = Arrangement.spacedBy((-10).dp)
                        ) {
                            repeat(13) {
                                PlayingCardView(
                                    card = Card(it, Suit.SPADES, Rank.ACE),
                                    isFaceUp = false,
                                    width = 18.dp,
                                    height = 26.dp
                                )
                            }
                        }

                        // Penalty Score e.g. 20
                        Text("$userPenaltyScore", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                        // Won -1000
                        Text("-1000", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bottom Footer: Joker indicator & Action Buttons (EXIT / START)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Joker Indicator
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Joker", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        PlayingCardView(
                            card = Card(998, Suit.HEARTS, Rank.TEN, isWildJoker = true),
                            isFaceUp = true,
                            width = 24.dp,
                            height = 34.dp
                        )
                    }

                    // Action Buttons (EXIT & START)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onExit,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.width(80.dp).height(38.dp)
                        ) {
                            Text("EXIT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onStartAgain,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.width(90.dp).height(38.dp)
                        ) {
                            Text("START", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
