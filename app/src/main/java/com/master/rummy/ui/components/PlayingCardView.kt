package com.master.rummy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.master.rummy.core.engine.Card
import com.master.rummy.ui.theme.CardBackground
import com.master.rummy.ui.theme.CardBorder
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.SuitBlack
import com.master.rummy.ui.theme.SuitRed

@Composable
fun PlayingCardView(
    card: Card,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isFaceUp: Boolean = true,
    width: Dp = 48.dp,
    height: Dp = 72.dp,
    onClick: (() -> Unit)? = null
) {
    val cardColor = if (card.suit.isRed) SuitRed else SuitBlack

    ElevatedCard(
        modifier = modifier
            .width(width)
            .height(height)
            .offset(y = if (isSelected) (-14).dp else 0.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .then(
                if (isSelected) Modifier.border(2.dp, Color(0xFF00E5FF), RoundedCornerShape(4.dp))
                else Modifier.border(0.5.dp, CardBorder, RoundedCornerShape(4.dp))
            ),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isFaceUp) CardBackground else Color(0xFFC8963E)
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        if (!isFaceUp) {
            // Authentic Gold Card Back from Device Screenshot
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFE2B755), Color(0xFFBF882C), Color(0xFFE5C16C))
                        )
                    )
                    .border(1.dp, Color(0xFF7A5210), RoundedCornerShape(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Gold spade filigree center
                Box(
                    modifier = Modifier
                        .size((width.value * 0.45f).dp)
                        .border(1.dp, Color(0xFF7A5210).copy(alpha = 0.5f), RoundedCornerShape(2.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "♠",
                        color = Color(0xFF7A5210),
                        fontSize = (width.value * 0.35f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            // Card Face
            Box(modifier = Modifier.fillMaxSize()) {
                // Top-left index: Rank and Suit
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 2.dp, top = 1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = card.rank.label,
                        color = cardColor,
                        fontSize = (height.value * 0.22f).sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = (height.value * 0.22f).sp
                    )
                    Text(
                        text = card.suit.symbol,
                        color = cardColor,
                        fontSize = (height.value * 0.18f).sp,
                        lineHeight = (height.value * 0.18f).sp
                    )
                }

                // Center large suit symbol
                Text(
                    text = card.suit.symbol,
                    color = cardColor,
                    fontSize = (height.value * 0.42f).sp,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 2.dp, bottom = 2.dp)
                )

                // Wild Joker or Printed Joker Hat Badge
                if (card.isWildJoker || card.isPrintedJoker) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                            .size((width.value * 0.35f).dp)
                            .clip(CircleShape)
                            .background(GoldAccent)
                            .border(0.5.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🃏",
                            fontSize = (width.value * 0.22f).sp
                        )
                    }
                }
            }
        }
    }
}
