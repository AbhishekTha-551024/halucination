package com.master.rummy.ui.refer

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.MaroonHeader

@Composable
fun ReferEarnScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedReferTab by remember { mutableIntStateOf(0) } // 0: Home, 1: History

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. TOP HEADER BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaroonHeader)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
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

                Text(
                    text = "REFER & EARN",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(32.dp))
            }
        }

        // 2. HERO BANNER (Ambassador + ₹1,100 + ₹150 CRORE+)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF3E0206), Color(0xFF6B0B11), Color(0xFF2E0407))
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Ambassador Graphic
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🤵‍♂️", fontSize = 38.sp)
                    }

                    // Main Promotional Text
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f).padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Each Invited Friend",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Brings You ", color = Color.White, fontWeight = FontWeight.Black, fontSize = 17.sp)
                            Text("₹1,100", color = GoldAccent, fontWeight = FontWeight.Black, fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // ₹150 CRORE+ Badge with Bulb Border
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF260205))
                                .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "PLAYERS HAVE COLLECTIVELY EARNED",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "₹150 CRORE+",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. SUB-TABS: Home vs History
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedReferTab = 0 }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Home",
                        color = if (selectedReferTab == 0) MaroonHeader else Color.Gray,
                        fontWeight = if (selectedReferTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (selectedReferTab == 0) {
                        Box(modifier = Modifier.width(60.dp).height(2.5.dp).background(MaroonHeader, RoundedCornerShape(1.dp)))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedReferTab = 1 }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "History",
                        color = if (selectedReferTab == 1) MaroonHeader else Color.Gray,
                        fontWeight = if (selectedReferTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (selectedReferTab == 1) {
                        Box(modifier = Modifier.width(60.dp).height(2.5.dp).background(MaroonHeader, RoundedCornerShape(1.dp)))
                    }
                }
            }
        }

        // 4. LIVE TICKER STRIP
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF8E1))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Vj*******************hi has already earned ",
                    fontSize = 11.sp,
                    color = Color(0xFF6A1B9A),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹996630! 🎉",
                    fontSize = 11.sp,
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.Black
                )
            }
        }

        HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)

        // 5. EMPTY REFERRAL STATE / CONTENT
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                // Yellow Document Graphic
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFD54F).copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📜", fontSize = 48.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No Referral Yet",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You have not any referrals till now.\nWin the biggest reward with every referral",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Share Via Button
                Button(
                    onClick = {
                        Toast.makeText(context, "Referral Link: https://rummy.game/invite?code=58930", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(38.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔗", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share Via",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
