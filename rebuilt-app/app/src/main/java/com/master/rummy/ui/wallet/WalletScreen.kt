package com.master.rummy.ui.wallet

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.master.rummy.ui.theme.GoldAccent
import com.master.rummy.ui.theme.MaroonHeader

data class DepositCardItem(
    val amount: Int,
    val bonus: Int,
    val isPopular: Boolean = false
)

@Composable
fun WalletScreen(
    currentCash: Double,
    onAddCashSuccess: (amount: Double) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var userCash by remember { mutableDoubleStateOf(currentCash) }
    var selectedDepositIndex by remember { mutableIntStateOf(0) } // Default 100
    var showPaymentMethodSheet by remember { mutableStateOf(false) }
    var selectedUpiOption by remember { mutableIntStateOf(0) } // 0: UPI 1, 1: UPI 2

    val depositOptions = listOf(
        DepositCardItem(100, 100, isPopular = true),
        DepositCardItem(300, 300),
        DepositCardItem(500, 500),
        DepositCardItem(1000, 1000),
        DepositCardItem(3000, 3000),
        DepositCardItem(5000, 5000)
    )

    val currentDeposit = depositOptions[selectedDepositIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFB))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP HEADER BAR (Exact Maroon Bar: < ADD CASH 🎧)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaroonHeader)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        text = "ADD CASH",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp
                    )

                    Text("🎧", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. 6 DEPOSIT CARDS (2 COLUMNS X 3 ROWS)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1: 100 & 300
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DepositCardView(
                        item = depositOptions[0],
                        isSelected = selectedDepositIndex == 0,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedDepositIndex = 0 }
                    )
                    DepositCardView(
                        item = depositOptions[1],
                        isSelected = selectedDepositIndex == 1,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedDepositIndex = 1 }
                    )
                }

                // Row 2: 500 & 1000
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DepositCardView(
                        item = depositOptions[2],
                        isSelected = selectedDepositIndex == 2,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedDepositIndex = 2 }
                    )
                    DepositCardView(
                        item = depositOptions[3],
                        isSelected = selectedDepositIndex == 3,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedDepositIndex = 3 }
                    )
                }

                // Row 3: 3000 & 5000
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DepositCardView(
                        item = depositOptions[4],
                        isSelected = selectedDepositIndex == 4,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedDepositIndex = 4 }
                    )
                    DepositCardView(
                        item = depositOptions[5],
                        isSelected = selectedDepositIndex == 5,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedDepositIndex = 5 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. COUPON BANNER ('WELCOME' applied!)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎁", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "'WELCOME' applied!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "others",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. CALCULATION STRIP (You Pay + Bonus = You Get)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("You Pay", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("₹${currentDeposit.amount}", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }

                Text("+", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF42A5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("B", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Bonus", color = Color.Gray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("₹${currentDeposit.bonus}", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }

                Text("=", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("You Get", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("₹${currentDeposit.amount + currentDeposit.bonus}", color = Color(0xFFC62828), fontWeight = FontWeight.Black, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 5. BIG VIBRANT GREEN ADD BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(
                    onClick = { showPaymentMethodSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A32A)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "ADD ₹${currentDeposit.amount}",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                }
            }

            // 6. 100% Safe and Secure subtext
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🛡️", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("100% safe and secure", color = Color.Gray, fontSize = 11.sp)
            }
        }

        // ==========================================
        // 7. EXACT PAYMENT METHOD BOTTOM SHEET
        // ==========================================
        if (showPaymentMethodSheet) {
            Dialog(
                onDismissRequest = { showPaymentMethodSheet = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.55f))
                        .clickable { showPaymentMethodSheet = false },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = false) { },
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Sheet Title & Close X
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Payment Method",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )

                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .clickable { showPaymentMethodSheet = false },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✕", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // UPI 1 vs UPI 2 Option Cards
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // UPI 1 (Selected)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(96.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF8F9FA))
                                        .border(
                                            width = if (selectedUpiOption == 0) 1.5.dp else 1.dp,
                                            color = if (selectedUpiOption == 0) Color(0xFF00A32A) else Color(0xFFE0E0E0),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedUpiOption = 0 }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("UPI", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF424242))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Paytm • PhonePe • GPay", color = Color.Gray, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("UPI 1", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Black)
                                    }
                                }

                                // UPI 2 (+5% Bonus)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(96.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF8F9FA))
                                        .border(
                                            width = if (selectedUpiOption == 1) 1.5.dp else 1.dp,
                                            color = if (selectedUpiOption == 1) Color(0xFF00A32A) else Color(0xFFE0E0E0),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedUpiOption = 1 }
                                        .padding(8.dp)
                                ) {
                                    // +5% Red Badge
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFE53935))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("+5%", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Column(
                                        modifier = Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Paytm / PhonePe", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0288D1))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("UPI 2", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Black)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Dummy Simulation Add Button
                            Button(
                                onClick = {
                                    showPaymentMethodSheet = false
                                    val added = currentDeposit.amount.toDouble()
                                    userCash += added
                                    onAddCashSuccess(added)
                                    Toast.makeText(context, "✅ ₹${currentDeposit.amount} Added Successfully! (Dummy Mode)", Toast.LENGTH_LONG).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A32A)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text(
                                    text = "ADD ₹${currentDeposit.amount}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🛡️", fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("100% safe and secure (Dummy Test Mode)", color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DepositCardView(
    item: DepositCardItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(88.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Color(0xFF00A32A) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        // Most Popular Yellow Tag
        if (item.isPopular) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clip(RoundedCornerShape(bottomEnd = 6.dp))
                    .background(Color(0xFFFFB300))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("❤️", fontSize = 8.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("MOST POPULAR", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = if (item.isPopular) 8.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gold Rupee Amount
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(GoldAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text("₹", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("₹${item.amount}", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Blue Diamond Bonus Amount
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF42A5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("B", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("₹${item.bonus}", color = Color(0xFF1565C0), fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
        }
    }
}
