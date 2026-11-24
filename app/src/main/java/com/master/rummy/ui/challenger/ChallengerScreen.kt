package com.master.rummy.ui.challenger

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.master.rummy.ui.theme.AddCashGreen
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.MaroonHeader

data class LeaderboardItem(
    val rankStr: String,
    val name: String,
    val scoreStr: String,
    val prizeStr: String,
    val avatarEmoji: String,
    val isMe: Boolean = false,
    val medalType: String? = null // "GOLD", "SILVER", "BRONZE"
)

@Composable
fun ChallengerScreen(
    userName: String = "player58930",
    onBack: () -> Unit,
    onOpenLobby: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Mini Challenger, 1: Mega Challenger

    val leaderboardData = listOf(
        LeaderboardItem(
            rankStr = "-",
            name = "Me",
            scoreStr = "0 Points",
            prizeStr = "-",
            avatarEmoji = "🧢",
            isMe = true
        ),
        LeaderboardItem(
            rankStr = "1",
            name = "ANJALI",
            scoreStr = "2784000 Points",
            prizeStr = "₹20000",
            avatarEmoji = "👩",
            medalType = "GOLD"
        ),
        LeaderboardItem(
            rankStr = "2",
            name = "zaxo",
            scoreStr = "2520000 Points",
            prizeStr = "₹15000",
            avatarEmoji = "😎",
            medalType = "SILVER"
        ),
        LeaderboardItem(
            rankStr = "3",
            name = "player06589",
            scoreStr = "2080000 Points",
            prizeStr = "₹10620",
            avatarEmoji = "👽",
            medalType = "BRONZE"
        ),
        LeaderboardItem(
            rankStr = "4",
            name = "vikram_99",
            scoreStr = "1890000 Points",
            prizeStr = "₹7500",
            avatarEmoji = "👨"
        ),
        LeaderboardItem(
            rankStr = "5",
            name = "RummyKing_21",
            scoreStr = "1640000 Points",
            prizeStr = "₹5000",
            avatarEmoji = "🤠"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP HEADER (Deep Maroon Bar with < CHALLENGER)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaroonHeader)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onBack() },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("‹", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "CHALLENGER",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(32.dp))
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 2. HERO BANNER: Red Spotlight Stage with "Total Prizes Issued 7.6Cr"
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF380004), Color(0xFF750A10), Color(0xFF4A0005))
                                )
                            )
                            .padding(top = 12.dp, bottom = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Total Prizes Issued",
                                color = GoldAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "7.6Cr",
                                color = Color(0xFFFFD54F),
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                // 3. WHITE PRIZE CARD (Mini + Mega Challenger Prizes)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Title row with • Live badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Mini + Mega Challenger Prizes",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(0xFF212121)
                                    )
                                    Text(
                                        text = "Mega + 30Minis,23rd Nov to 22nd Dec",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(AddCashGreen)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Live",
                                        color = AddCashGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Prize Amount & PLAY NOW Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🏆", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "₹75L",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF1E293B)
                                    )
                                }

                                Button(
                                    onClick = onOpenLobby,
                                    colors = ButtonDefaults.buttonColors(containerColor = AddCashGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Text(
                                        text = "PLAY NOW",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Text(
                                text = "*Fees: 3% of cash game winnings",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Filter tags row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf("All variants", "👥 All Player(s)", "₹ Entry Fee: All Values").forEach { tag ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFF1F5F9))
                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = tag, color = Color(0xFF475569), fontSize = 9.sp)
                                    }
                                }
                                Text("↪", fontSize = 12.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.8.dp)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Card Footer: Red timer & View Schedule
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Current challenger ends in ", color = Color.Black, fontSize = 11.sp)
                                    Text("10:28:47", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Text(
                                    text = "View Schedule",
                                    color = Color(0xFF1E293B),
                                    fontSize = 11.sp,
                                    textDecoration = TextDecoration.Underline,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // 4. SUB-TABS: Mini Challenger | Mega Challenger
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFCEFEF))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Mini Challenger",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (selectedTab == 0) Color(0xFF830C12) else Color.Gray
                                )
                                if (selectedTab == 0) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(3.dp)
                                            .background(Color(0xFF830C12))
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = 1 }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Mega Challenger",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (selectedTab == 1) Color(0xFF830C12) else Color.Gray
                                )
                                if (selectedTab == 1) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(3.dp)
                                            .background(Color(0xFF830C12))
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. DROPDOWN FILTERS ROW
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Time Period: 24th Nov 0:00am to 23:59pm ▼",
                            color = Color(0xFF333333),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Total Prize: ₹1.5L ▼",
                            color = Color(0xFF333333),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // 6. LEADERBOARD TABLE HEADER
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEEEEEE))
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Rank", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Player/Score", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Expected Prize", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // 7. LEADERBOARD ROWS
                itemsIndexed(leaderboardData) { _, item ->
                    val rowBg = if (item.isMe) Color(0xFFFFFDE7) else Color.White
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowBg)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank / Medal column
                            Box(
                                modifier = Modifier.width(42.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                when (item.medalType) {
                                    "GOLD" -> {
                                        Text("🥇", fontSize = 22.sp)
                                    }
                                    "SILVER" -> {
                                        Text("🥈", fontSize = 22.sp)
                                    }
                                    "BRONZE" -> {
                                        Text("🥉", fontSize = 22.sp)
                                    }
                                    else -> {
                                        Text(
                                            text = item.rankStr,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }

                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFFB0BEC5), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.avatarEmoji, fontSize = 18.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Name & Score column
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = item.scoreStr,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }

                            // Expected Prize column
                            Text(
                                text = item.prizeStr,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (item.prizeStr.startsWith("₹")) Color.Black else Color.Gray
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F1F1), thickness = 0.8.dp)
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // 8. FLOATING BOTTOM PILL & OPT-IN TEXT
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Floating Pill (Win ₹75L Cash Prize | End in: 10:28:47 | Play Now)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .shadow(8.dp, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Win ₹75L Cash Prize",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF212121)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("End in: ", color = Color.Gray, fontSize = 11.sp)
                            Text("10:28:47", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenLobby,
                        colors = ButtonDefaults.buttonColors(containerColor = AddCashGreen),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Play Now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Automatically Opt-in for future challengers",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}
