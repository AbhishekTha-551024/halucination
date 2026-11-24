package com.master.rummy.ui.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.master.rummy.ui.theme.AddCashGreen
import com.master.rummy.ui.theme.MaroonHeader

@Composable
fun ProfileScreen(
    userName: String = "player58930",
    cashBalance: Double = 6.73,
    chipBalance: Long = 10349L,
    onLogout: () -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFB))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP HEADER (Deep Maroon Header matching user_opened_profile.png)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaroonHeader)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onBack() },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("‹", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Avatar Circle with Pencil Badge
                    Box(
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧢", fontSize = 34.sp)
                        }

                        // Pencil edit badge
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✏️", fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Username & ID
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "ID copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text(
                                text = "ID: 23294503",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "❐",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Diamond Badge on right
                    Text(
                        text = "❖",
                        color = Color(0xFFB0BEC5),
                        fontSize = 26.sp
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // 2. MY WALLET SECTION HEADER
                Text(
                    text = "My Wallet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // WALLET MAIN CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Playable Balance Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Playable Balance", color = Color.Gray, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "₹" + String.format("%.2f", cashBalance),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "View All Transactions ›",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable {
                                    Toast.makeText(context, "Opening transaction records", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Deposit Balance Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("👛", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Deposit Balance", color = Color.Gray, fontSize = 12.sp)
                                Text(
                                    text = "₹" + String.format("%.2f", cashBalance),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Deposit money selected", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AddCashGreen),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("ADD CASH", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Withdrawal Balance Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏧", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Withdrawal Balance", color = Color.Gray, fontSize = 12.sp)
                                Text(
                                    text = "₹0.00",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Min withdrawal ₹100", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333333)),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("WITHDRAW", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BONUS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1976D2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("B", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Bonus", color = Color.Gray, fontSize = 12.sp)
                            Text("19.81", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. PERSONAL INFORMATION SECTION HEADER
                Text(
                    text = "Personal Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // PERSONAL INFORMATION CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                        // Mobile Number
                        ProfileInfoRow(
                            icon = "📱",
                            label = "Mobile Number",
                            badgeText = "Verified",
                            value = "8815114272",
                            buttonText = "CHANGE",
                            onAction = { Toast.makeText(context, "Change mobile requested", Toast.LENGTH_SHORT).show() }
                        )
                        HorizontalDivider(color = Color(0xFFEEEEEE))

                        // Email
                        ProfileInfoRow(
                            icon = "✉️",
                            label = "Email",
                            value = "Not added",
                            isValueGray = true,
                            buttonText = "ADD",
                            onAction = { Toast.makeText(context, "Add email dialog", Toast.LENGTH_SHORT).show() }
                        )
                        HorizontalDivider(color = Color(0xFFEEEEEE))

                        // Telegram Helper
                        ProfileInfoRow(
                            icon = "✈️",
                            label = "Telegram Helper",
                            value = "Not added",
                            isValueGray = true,
                            buttonText = "ADD",
                            onAction = { Toast.makeText(context, "Link Telegram bot", Toast.LENGTH_SHORT).show() }
                        )
                        HorizontalDivider(color = Color(0xFFEEEEEE))

                        // Date of birth
                        ProfileInfoRow(
                            icon = "📅",
                            label = "Date of birth",
                            value = "Not added",
                            isValueGray = true,
                            buttonText = "ADD",
                            onAction = { Toast.makeText(context, "Select DOB", Toast.LENGTH_SHORT).show() }
                        )
                        HorizontalDivider(color = Color(0xFFEEEEEE))

                        // Gender
                        ProfileInfoRow(
                            icon = "⚥",
                            label = "Gender",
                            value = "Male",
                            buttonText = "EDIT",
                            onAction = { Toast.makeText(context, "Edit gender", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // CLUB CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("❖", fontSize = 24.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Club", color = Color.Gray, fontSize = 12.sp)
                            Text("Next level:5/1000", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        }
                        OutlinedButton(
                            onClick = { Toast.makeText(context, "Club benefits details", Toast.LENGTH_SHORT).show() },
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333333)),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("CHECK", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    icon: String,
    label: String,
    badgeText: String? = null,
    value: String,
    isValueGray: Boolean = false,
    buttonText: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, color = Color.Gray, fontSize = 12.sp)
                if (badgeText != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(text = badgeText, color = AddCashGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontWeight = if (isValueGray) FontWeight.Normal else FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isValueGray) Color.Gray else Color.Black
            )
        }

        OutlinedButton(
            onClick = onAction,
            shape = RoundedCornerShape(4.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333333)),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(text = buttonText, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}
