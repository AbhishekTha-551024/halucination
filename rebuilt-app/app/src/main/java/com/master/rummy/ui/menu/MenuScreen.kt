package com.master.rummy.ui.menu

import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.master.rummy.ui.theme.AddCashGreen
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.MaroonHeader

@Composable
fun MenuScreen(
    userName: String = "player58930",
    cashBalance: Double = 7.66,
    chipBalance: Long = 10349L,
    onOpenProfile: () -> Unit = {},
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    var bgmEnabled by remember { mutableStateOf(true) }
    var sfxEnabled by remember { mutableStateOf(true) }
    var autoSortEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }

    var showRulesDialog by remember { mutableStateOf(false) }
    var showRngDialog by remember { mutableStateOf(false) }
    var showRecordsDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showResponsibleGamingDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP HEADER (Deep Maroon Bar with < MENU & SETTINGS)
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
                        text = "MENU & SETTINGS",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(32.dp))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(14.dp)
            ) {
                // 2. USER PROFILE SUMMARY CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenProfile() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🧢", fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ID: 5893021 • Cash: ₹" + String.format("%.2f", cashBalance),
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }

                        Text("View Profile ›", color = Color(0xFF00A32A), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. AUDIO & CONTROLS SETTINGS (from UISettings.ts)
                Text(
                    text = "Game Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Background Music", fontSize = 13.sp, color = Color.Black)
                            Switch(
                                checked = bgmEnabled,
                                onCheckedChange = { bgmEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AddCashGreen)
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sound Effects", fontSize = 13.sp, color = Color.Black)
                            Switch(
                                checked = sfxEnabled,
                                onCheckedChange = { sfxEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AddCashGreen)
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Auto-Sort Hand", fontSize = 13.sp, color = Color.Black)
                            Switch(
                                checked = autoSortEnabled,
                                onCheckedChange = { autoSortEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AddCashGreen)
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Vibration / Haptic", fontSize = 13.sp, color = Color.Black)
                            Switch(
                                checked = vibrationEnabled,
                                onCheckedChange = { vibrationEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AddCashGreen)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. INFORMATION & SUPPORT LIST
                Text(
                    text = "Help & Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                        MenuItemRow("📖", "Game Rules & How to Play") { showRulesDialog = true }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        MenuItemRow("🛡️", "Fair Play & RNG Certificate") { showRngDialog = true }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        MenuItemRow("📜", "Transaction & Game Records") { showRecordsDialog = true }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        MenuItemRow("⚖️", "Responsible Gaming Guidelines") { showResponsibleGamingDialog = true }
                        HorizontalDivider(color = Color(0xFFF1F1F1))

                        MenuItemRow("🎧", "24x7 Customer Support") { showSupportDialog = true }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 5. LOGOUT BUTTON
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("LOGOUT / RESET SESSION", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer version
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Junglee Rummy v1.0.4 (Secure Build 2026)",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Dialogs
        if (showRulesDialog) {
            AlertDialog(
                onDismissRequest = { showRulesDialog = false },
                title = { Text("13-Card Indian Rummy Rules", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "1. Objective: Arrange all 13 cards into valid sequences and sets.\n\n" +
                        "2. Pure Sequence: At least one sequence without any wild Joker (e.g. 4♠ 5♠ 6♠) is mandatory.\n\n" +
                        "3. Impure Sequence: A sequence made with one or more Jokers.\n\n" +
                        "4. Sets: Three or four cards of the same face value with different suits (e.g. 7♦ 7♠ 7♥).\n\n" +
                        "5. Declaration: Valid declare with at least 1 Pure Sequence and deadwood score of 0 wins!"
                    )
                },
                confirmButton = {
                    Button(onClick = { showRulesDialog = false }) { Text("Got It") }
                }
            )
        }

        if (showRngDialog) {
            AlertDialog(
                onDismissRequest = { showRngDialog = false },
                title = { Text("Certified Fair Play & RNG", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Our Random Number Generator (RNG) is certified by independent testing laboratories (iTech Labs) " +
                        "ensuring 100% unpredictable, non-repeatable, and completely fair card distribution for all players."
                    )
                },
                confirmButton = {
                    Button(onClick = { showRngDialog = false }) { Text("Close") }
                }
            )
        }

        if (showRecordsDialog) {
            AlertDialog(
                onDismissRequest = { showRecordsDialog = false },
                title = { Text("Game Records", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "• Match #14168260: Points Rummy (Won +₹16.00)\n" +
                        "• Match #14168258: Pool 101 (Dropped -₹4.00)\n" +
                        "• Match #14168252: Points Rummy (Won +₹24.00)\n\n" +
                        "All games verified on fair-play ledger."
                    )
                },
                confirmButton = {
                    Button(onClick = { showRecordsDialog = false }) { Text("OK") }
                }
            )
        }

        if (showSupportDialog) {
            AlertDialog(
                onDismissRequest = { showSupportDialog = false },
                title = { Text("24x7 Customer Support", fontWeight = FontWeight.Bold) },
                text = {
                    Text("Our dedicated support team is available 24x7.\n\nEmail: support@masterummy.com\nLive Chat: Available inside table or wallet.")
                },
                confirmButton = {
                    Button(onClick = { showSupportDialog = false }) { Text("Close") }
                }
            )
        }

        if (showResponsibleGamingDialog) {
            AlertDialog(
                onDismissRequest = { showResponsibleGamingDialog = false },
                title = { Text("Responsible Gaming", fontWeight = FontWeight.Bold) },
                text = {
                    Text("Rummy is a game of skill. Please play responsibly within your limits. Daily deposit limits and self-exclusion tools are supported.")
                },
                confirmButton = {
                    Button(onClick = { showResponsibleGamingDialog = false }) { Text("Understand") }
                }
            )
        }
    }
}

@Composable
fun MenuItemRow(icon: String, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 13.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Text("›", color = Color.Gray, fontSize = 18.sp)
    }
}
