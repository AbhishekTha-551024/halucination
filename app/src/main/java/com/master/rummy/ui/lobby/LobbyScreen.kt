package com.master.rummy.ui.lobby

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.master.rummy.ui.theme.AddCashGreen
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.LightGreyButton
import com.master.rummy.ui.theme.MaroonActiveCard
import com.master.rummy.ui.theme.MaroonHeader
import com.master.rummy.ui.theme.MaroonSubTabUnderline
import com.master.rummy.ui.theme.PlayNowGreen
import com.master.rummy.ui.theme.SubTabBackground

data class RoomTier(
    val id: String,
    val name: String,
    val pointValue: String,
    val minEntry: String,
    val activePlayers: Int,
    val isPractice: Boolean = false
)

enum class MainMode {
    CASH,
    TOURNAMENTS,
    PRACTICE
}

data class TournamentItem(
    val id: String,
    val title: String,
    val prize: String,
    val entry: String,
    val playersJoined: String,
    val startTime: String,
    val isFree: Boolean = false
)

@Composable
fun LobbyScreen(
    userName: String,
    cashBalance: Double,
    chipBalance: Long,
    onJoinTable: (tier: RoomTier) -> Unit,
    onOpenWallet: () -> Unit,
    onOpenChallenger: () -> Unit,
    onOpenReferEarn: () -> Unit,
    onOpenMenu: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val context = LocalContext.current
    var activeMode by remember { mutableStateOf(MainMode.CASH) }
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Points, 1: Pool, 2: Deals
    var selectedPlayerCount by remember { mutableIntStateOf(2) } // 2 or 6
    var selectedGameType by remember { mutableIntStateOf(0) } // 0: Classic, 1: Double
    var selectedPoolType by remember { mutableIntStateOf(0) } // 0: 101 Pool, 1: 201 Pool
    var selectedDealsType by remember { mutableIntStateOf(0) } // 0: Best of 2, 1: Best of 3
    var sliderIndex by remember { mutableFloatStateOf(0f) }

    // Dialog States
    var showDailyBonusDialog by remember { mutableStateOf(false) }
    var showGiftOfferDialog by remember { mutableStateOf(false) }

    // Points Stakes
    val cashStakes = listOf(
        Pair("0.05", "4"),
        Pair("0.1", "8"),
        Pair("0.25", "20"),
        Pair("0.5", "40"),
        Pair("1", "80"),
        Pair("2", "160"),
        Pair("5", "400"),
        Pair("10", "800"),
        Pair("25", "2000"),
        Pair("50", "4000"),
        Pair("125", "10000")
    )

    val practiceStakes = listOf(
        Pair("20", "1600"),
        Pair("50", "4000"),
        Pair("100", "8000"),
        Pair("500", "40000"),
        Pair("1000", "80000"),
        Pair("5000", "4.00L"),
        Pair("10000", "10.00L")
    )

    // Pool Stakes
    val poolStakes = listOf(
        Pair("10", "10"),
        Pair("25", "25"),
        Pair("50", "50"),
        Pair("100", "100"),
        Pair("250", "250")
    )

    // Deals Stakes
    val dealsStakes = listOf(
        Pair("15", "15"),
        Pair("50", "50"),
        Pair("100", "100"),
        Pair("200", "200")
    )

    val currentStakes = when {
        activeMode == MainMode.PRACTICE -> practiceStakes
        selectedSubTab == 1 -> poolStakes
        selectedSubTab == 2 -> dealsStakes
        else -> cashStakes
    }

    val maxSliderIndex = (currentStakes.size - 1).toFloat()
    val clampedIndex = sliderIndex.coerceIn(0f, maxSliderIndex).toInt()
    val currentStake = currentStakes[clampedIndex]

    // Tournaments List
    val tournaments = listOf(
        TournamentItem("t1", "Grand Rummy Daily", "₹1,00,000", "₹50", "240 / 500", "Starts in 10m"),
        TournamentItem("t2", "Daily Freeroll Cup", "₹10,000", "FREE", "890 / 1000", "Registration Open", isFree = true),
        TournamentItem("t3", "Super Speed 50K", "₹50,000", "₹25", "150 / 300", "Starts in 25m"),
        TournamentItem("t4", "Sunday Mega Slam", "₹5,00,000", "₹250", "68 / 200", "Reg Open")
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // ==========================================
        // 1. TOP HEADER BAR (Exact Maroon Bar)
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaroonHeader)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile & Unranked Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenProfile() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧢", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = userName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF333333))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("◆", color = Color(0xFFB0BEC5), fontSize = 8.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Unranked", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                // Balance Pill (₹ 7.66 ⌄ or chips)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2E0407))
                        .border(1.dp, Color(0xFF5D1217), RoundedCornerShape(16.dp))
                        .clickable { onOpenWallet() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(GoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (activeMode == MainMode.PRACTICE) "🪙" else "₹",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (activeMode == MainMode.PRACTICE) "$chipBalance" else String.format("%.2f", cashBalance),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "⌄",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Green ADD CASH Button & Notification Bell
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onOpenWallet,
                        colors = ButtonDefaults.buttonColors(containerColor = AddCashGreen),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("₹", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ADD CASH",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Bell icon with red dot (Opens daily bonus)
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { showDailyBonusDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 18.sp)
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }

        // ==========================================
        // MAIN SCROLLABLE CONTENT
        // ==========================================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // 2. PROMO BANNER (Refer & Earn ₹300,000 / Daily Rewards)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (activeMode == MainMode.PRACTICE) {
                            Brush.horizontalGradient(listOf(Color(0xFFFF5722), Color(0xFFFF9800), Color(0xFFFFB300)))
                        } else {
                            Brush.horizontalGradient(listOf(Color(0xFF4A0207), Color(0xFF7A0912), Color(0xFF2E0407)))
                        }
                    )
                    .clickable {
                        if (activeMode == MainMode.PRACTICE) showDailyBonusDialog = true else onOpenReferEarn()
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (activeMode == MainMode.PRACTICE) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "DAILY REWARDS",
                                color = GoldAccent,
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Log in Daily to Receive Rich Rewards",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text("💰🎁", fontSize = 28.sp)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "REFER & EARN",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "₹300000* CASH",
                                color = GoldAccent,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                        Text("💵 ₹500 💵", fontSize = 20.sp)
                    }
                }
            }

            // 3. MODE SELECTOR (3 CARDS: CASH, TOURNAMENTS, PRACTICE)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Card 1: CASH
                ModeCard(
                    title = "CASH",
                    icon = "₹",
                    isSelected = activeMode == MainMode.CASH,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        activeMode = MainMode.CASH
                        sliderIndex = 0f
                    }
                )

                // Card 2: TOURNAMENTS
                ModeCard(
                    title = "TOURNAMENTS",
                    icon = "🏆",
                    isSelected = activeMode == MainMode.TOURNAMENTS,
                    modifier = Modifier.weight(1f),
                    onClick = { activeMode = MainMode.TOURNAMENTS }
                )

                // Card 3: PRACTICE
                ModeCard(
                    title = "PRACTICE",
                    icon = "🎯",
                    isSelected = activeMode == MainMode.PRACTICE,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        activeMode = MainMode.PRACTICE
                        sliderIndex = 0f
                    }
                )
            }

            // Practice Chips Badge (shown when practice mode is selected)
            if (activeMode == MainMode.PRACTICE) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEBEBEB))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🪙", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$chipBalance",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. SUB-TABS (Points, Pool, Deals)
            if (activeMode != MainMode.TOURNAMENTS) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SubTabBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("Points", "Pool", "Deals").forEachIndexed { index, title ->
                            val isSelected = selectedSubTab == index
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        selectedSubTab = index
                                        sliderIndex = 0f
                                    }
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) MaroonSubTabUnderline else Color(0xFF666666),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(3.dp)
                                            .background(MaroonSubTabUnderline, RoundedCornerShape(2.dp))
                                    )
                                } else {
                                    Box(modifier = Modifier.height(3.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5. CONFIGURATION CARD (Points, Pool, or Deals)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Options Row depending on Sub-Tab
                        when (selectedSubTab) {
                            0 -> {
                                // POINTS RUMMY
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Select Players",
                                        color = Color(0xFF555555),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center
                                    )
                                    if (activeMode != MainMode.PRACTICE) {
                                        Text(
                                            text = "Game Type",
                                            color = Color(0xFF555555),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Players [ 2 ] [ 6 ]
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxSize()
                                                .background(if (selectedPlayerCount == 2) MaroonActiveCard else LightGreyButton)
                                                .clickable { selectedPlayerCount = 2 },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "2",
                                                color = if (selectedPlayerCount == 2) Color.White else Color.Black,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxSize()
                                                .background(if (selectedPlayerCount == 6) MaroonActiveCard else LightGreyButton)
                                                .clickable { selectedPlayerCount = 6 },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "6",
                                                color = if (selectedPlayerCount == 6) Color.White else Color.Black,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }

                                    // Game Type [ Classic ] [ Double ]
                                    if (activeMode != MainMode.PRACTICE) {
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(38.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxSize()
                                                    .background(if (selectedGameType == 0) MaroonActiveCard else LightGreyButton)
                                                    .clickable { selectedGameType = 0 },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Classic",
                                                    color = if (selectedGameType == 0) Color.White else Color.Black,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxSize()
                                                    .background(if (selectedGameType == 1) MaroonActiveCard else LightGreyButton)
                                                    .clickable { selectedGameType = 1 },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Double",
                                                    color = if (selectedGameType == 1) Color.White else Color.Black,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                // POOL RUMMY (101 Pool vs 201 Pool)
                                Text("Select Pool Target", color = Color(0xFF555555), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxSize()
                                            .background(if (selectedPoolType == 0) MaroonActiveCard else LightGreyButton)
                                            .clickable { selectedPoolType = 0 },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("101 Pool", color = if (selectedPoolType == 0) Color.White else Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxSize()
                                            .background(if (selectedPoolType == 1) MaroonActiveCard else LightGreyButton)
                                            .clickable { selectedPoolType = 1 },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("201 Pool", color = if (selectedPoolType == 1) Color.White else Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            }

                            2 -> {
                                // DEALS RUMMY (2 Deals vs 3 Deals)
                                Text("Select Number of Deals", color = Color(0xFF555555), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxSize()
                                            .background(if (selectedDealsType == 0) MaroonActiveCard else LightGreyButton)
                                            .clickable { selectedDealsType = 0 },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("2 Deals", color = if (selectedDealsType == 0) Color.White else Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxSize()
                                            .background(if (selectedDealsType == 1) MaroonActiveCard else LightGreyButton)
                                            .clickable { selectedDealsType = 1 },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("3 Deals", color = if (selectedDealsType == 1) Color.White else Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Point Value / Entry Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (selectedSubTab == 0) "Point Value " else "Entry Fee ",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (activeMode == MainMode.PRACTICE) currentStake.first else "₹${currentStake.first}",
                                color = Color(0xFF830C12),
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("›", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Min Entry ",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (activeMode == MainMode.PRACTICE) currentStake.second else "₹${currentStake.second}",
                                color = Color(0xFF830C12),
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Slider with Circular Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Minus Button
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color(0xFFBDBDBD), CircleShape)
                                    .clickable {
                                        if (sliderIndex > 0) sliderIndex -= 1f
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("—", color = Color(0xFF424242), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }

                            // Slider
                            Slider(
                                value = sliderIndex,
                                onValueChange = { sliderIndex = it },
                                valueRange = 0f..maxSliderIndex,
                                steps = (currentStakes.size - 2).coerceAtLeast(0),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF00A32A),
                                    activeTrackColor = Color(0xFF00A32A),
                                    inactiveTrackColor = Color(0xFFCCCCCC)
                                )
                            )

                            // Plus Button
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, Color(0xFF00A32A), CircleShape)
                                    .clickable {
                                        if (sliderIndex < maxSliderIndex) sliderIndex += 1f
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color(0xFF00A32A), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Big Vibrant Green PLAY NOW Button
                        Button(
                            onClick = {
                                val tier = RoomTier(
                                    id = "table_${currentStake.first}",
                                    name = when {
                                        activeMode == MainMode.PRACTICE -> "Practice Table"
                                        selectedSubTab == 1 -> "101 Pool Table"
                                        selectedSubTab == 2 -> "Deals Table"
                                        else -> "Points Table"
                                    },
                                    pointValue = currentStake.first,
                                    minEntry = currentStake.second,
                                    activePlayers = selectedPlayerCount,
                                    isPractice = activeMode == MainMode.PRACTICE
                                )
                                onJoinTable(tier)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PlayNowGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .width(220.dp)
                                .height(48.dp)
                                .shadow(4.dp, RoundedCornerShape(10.dp))
                        ) {
                            Text(
                                text = "PLAY NOW",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            } else {
                // TOURNAMENTS MODE LIST VIEW
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("LIVE & UPCOMING TOURNAMENTS", fontWeight = FontWeight.Black, fontSize = 13.sp, color = Color.DarkGray)

                    tournaments.forEach { t ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(t.title, fontWeight = FontWeight.Black, fontSize = 15.sp, color = MaroonHeader)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (t.isFree) Color(0xFFE8F5E9) else Color(0xFFFFF8E1))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (t.isFree) "FREE ENTRY" else "ENTRY ${t.entry}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = if (t.isFree) Color(0xFF2E7D32) else MaroonHeader
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Prize Pool: ${t.prize}", fontWeight = FontWeight.Black, fontSize = 13.sp, color = Color(0xFF2E7D32))
                                        Text("Players: ${t.playersJoined} • ${t.startTime}", color = Color.Gray, fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = {
                                            val tier = RoomTier("tourney_${t.id}", t.title, "10", t.entry, 6, false)
                                            onJoinTable(tier)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaroonHeader),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text("JOIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Floating Badges preview (Insane Loot & Gift)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { showGiftOfferDialog = true }
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE0E0E0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎁", fontSize = 24.sp)
                    }
                    Text("GIFT", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // ==========================================
        // 6. BOTTOM NAVIGATION (5 Tabs with Indicator)
        // ==========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(width = 0.5.dp, color = Color(0xFFE0E0E0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 1: Add Cash
                BottomNavItem(
                    label = "Add Cash",
                    icon = "₹+",
                    isSelected = false,
                    onClick = onOpenWallet
                )

                // Tab 2: Challenger
                BottomNavItem(
                    label = "Challenger",
                    icon = "⭐",
                    isSelected = false,
                    onClick = onOpenChallenger
                )

                // Tab 3: Lobby (Highlighted)
                BottomNavItem(
                    label = "Lobby",
                    icon = "♠",
                    isSelected = true,
                    isSpecialLobby = true,
                    onClick = { }
                )

                // Tab 4: Refer & Earn (With 300000 Badge)
                BottomNavItem(
                    label = "Refer & Earn",
                    icon = "👥",
                    badge = "300000",
                    isSelected = false,
                    onClick = onOpenReferEarn
                )

                // Tab 5: Menu
                BottomNavItem(
                    label = "Menu",
                    icon = "☰",
                    isSelected = false,
                    onClick = onOpenMenu
                )
            }
        }
    }

    // ==========================================
    // 7. DAILY BONUS DIALOG (Days 1 to 7 Rewards)
    // ==========================================
    if (showDailyBonusDialog) {
        Dialog(
            onDismissRequest = { showDailyBonusDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .clip(RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6B0B11))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(
                            text = "DAILY BONUS",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { showDailyBonusDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✕", color = Color.White, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Days 1 to 7 Cards
                    listOf(
                        Pair("DAY 1", "X10"),
                        Pair("DAY 2", "X15"),
                        Pair("DAY 3", "X20"),
                        Pair("DAY 4", "X25"),
                        Pair("DAY 5", "X30"),
                        Pair("DAY 6", "X35"),
                        Pair("DAY 7", "X40")
                    ).forEachIndexed { idx, day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (idx == 0) Color(0xFFFF9800) else Color.White)
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = day.first,
                                color = if (idx == 0) Color.White else MaroonHeader,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🪙", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = day.second,
                                    color = if (idx == 0) Color.White else Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            showDailyBonusDialog = false
                            Toast.makeText(context, "10 Chips Daily Bonus Collected!", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C851)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Text("COLLECT", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // ==========================================
    // 8. GIFT / 24H SUPER OFFERS DIALOG
    // ==========================================
    if (showGiftOfferDialog) {
        Dialog(
            onDismissRequest = { showGiftOfferDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎁 24H SUPER OFFERS", color = GoldAccent, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Text("ONE-TIME EXCLUSIVE OFFER!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text("Deposit ₹500 & Get:", color = Color.LightGray, fontSize = 12.sp)
                            Text("• ₹500 Extra Instant Bonus", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("• 1x Free Grand Tournament Ticket", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("• VIP Crown Badge on Table", color = Color.White, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showGiftOfferDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Later", color = Color.White)
                        }

                        Button(
                            onClick = {
                                showGiftOfferDialog = false
                                onOpenWallet()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AddCashGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("CLAIM NOW", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    icon: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) MaroonActiveCard else Color.White
            ),
            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)) else null,
            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, if (isSelected) Color.White else Color(0xFF666666), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        color = if (isSelected) Color.White else Color(0xFF444444),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    color = if (isSelected) Color.White else Color(0xFF444444),
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }
        }

        // Downward Triangle Pointer when Selected
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp)
                    .size(12.dp)
                    .clip(
                        GenericShape { size, _ ->
                            moveTo(0f, 0f)
                            lineTo(size.width, 0f)
                            lineTo(size.width / 2, size.height)
                            close()
                        }
                    )
                    .background(MaroonActiveCard)
            )
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: String,
    badge: String? = null,
    isSelected: Boolean = false,
    isSpecialLobby: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSelected && isSpecialLobby) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(2.5.dp)
                    .background(MaroonActiveCard, RoundedCornerShape(1.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Spacer(modifier = Modifier.height(6.5.dp))
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                text = icon,
                color = if (isSelected && isSpecialLobby) MaroonActiveCard else Color(0xFF666666),
                fontSize = if (isSpecialLobby) 20.sp else 16.sp,
                fontWeight = FontWeight.Bold
            )

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .offset(x = 18.dp, y = (-8).dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Red)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (isSelected && isSpecialLobby) MaroonActiveCard else Color(0xFF666666),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
