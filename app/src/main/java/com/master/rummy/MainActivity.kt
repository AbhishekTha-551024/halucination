package com.master.rummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.master.rummy.ui.challenger.ChallengerScreen
import com.master.rummy.ui.lobby.LobbyScreen
import com.master.rummy.ui.lobby.RoomTier
import com.master.rummy.ui.menu.MenuScreen
import com.master.rummy.ui.profile.ProfileScreen
import com.master.rummy.ui.refer.ReferEarnScreen
import com.master.rummy.ui.splash.SplashScreen
import com.master.rummy.ui.table.GameTableScreen
import com.master.rummy.ui.theme.MaroonHeader
import com.master.rummy.ui.theme.MasterRummyTheme
import com.master.rummy.ui.wallet.WalletScreen

enum class AppDestination {
    SPLASH,
    LOBBY,
    GAME_TABLE,
    WALLET,
    CHALLENGER,
    REFER_EARN,
    MENU,
    PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterRummyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaroonHeader
                ) {
                    RummyAppNavHost()
                }
            }
        }
    }
}

@Composable
fun RummyAppNavHost() {
    // Directly start in LOBBY with guest player58930 - no login or mobile number screen
    var destination by remember { mutableStateOf(AppDestination.LOBBY) }
    var currentUserName by remember { mutableStateOf("player58930") }
    var cashBalance by remember { mutableDoubleStateOf(7.66) }
    var chipBalance by remember { mutableLongStateOf(10349L) }
    var selectedTier by remember {
        mutableStateOf(
            RoomTier("p_1", "Bronze Table", "0.05", "4", 856)
        )
    }

    when (destination) {
        AppDestination.SPLASH -> {
            SplashScreen(
                onNavigateNext = { destination = AppDestination.LOBBY }
            )
        }
        AppDestination.LOBBY -> {
            LobbyScreen(
                userName = currentUserName,
                cashBalance = cashBalance,
                chipBalance = chipBalance,
                onJoinTable = { tier ->
                    selectedTier = tier
                    destination = AppDestination.GAME_TABLE
                },
                onOpenWallet = { destination = AppDestination.WALLET },
                onOpenChallenger = { destination = AppDestination.CHALLENGER },
                onOpenReferEarn = { destination = AppDestination.REFER_EARN },
                onOpenMenu = { destination = AppDestination.MENU },
                onOpenProfile = { destination = AppDestination.PROFILE }
            )
        }
        AppDestination.GAME_TABLE -> {
            GameTableScreen(
                tier = selectedTier,
                userName = currentUserName,
                onLeaveTable = { destination = AppDestination.LOBBY }
            )
        }
        AppDestination.WALLET -> {
            WalletScreen(
                currentCash = cashBalance,
                onAddCashSuccess = { added ->
                    cashBalance += added
                },
                onBack = { destination = AppDestination.LOBBY }
            )
        }
        AppDestination.CHALLENGER -> {
            ChallengerScreen(
                userName = currentUserName,
                onBack = { destination = AppDestination.LOBBY },
                onOpenLobby = { destination = AppDestination.LOBBY }
            )
        }
        AppDestination.REFER_EARN -> {
            ReferEarnScreen(
                onBack = { destination = AppDestination.LOBBY }
            )
        }
        AppDestination.MENU -> {
            MenuScreen(
                userName = currentUserName,
                cashBalance = cashBalance,
                chipBalance = chipBalance,
                onOpenProfile = { destination = AppDestination.PROFILE },
                onLogout = {
                    currentUserName = "player58930"
                    cashBalance = 7.66
                    chipBalance = 10349L
                    destination = AppDestination.LOBBY
                },
                onBack = { destination = AppDestination.LOBBY }
            )
        }
        AppDestination.PROFILE -> {
            ProfileScreen(
                userName = currentUserName,
                cashBalance = cashBalance,
                chipBalance = chipBalance,
                onLogout = {
                    currentUserName = "player58930"
                    cashBalance = 7.66
                    chipBalance = 10349L
                    destination = AppDestination.LOBBY
                },
                onBack = { destination = AppDestination.LOBBY }
            )
        }
    }
}
