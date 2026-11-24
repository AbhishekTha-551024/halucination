# Rebuild Plan: Indian Rummy Android App

**Target App Rebuild:** Modern native re-implementation of Junglee Rummy (`com.rummygamein.gameprjrumm`)  
**Architecture:** 100% Native Android (Kotlin + Jetpack Compose + MVVM + Clean Architecture)  
**Status:** Waiting for user "go" before starting Phase 3 (Build)

---

## 1. Tech Stack & Dependencies

| Layer | Selected Technology | Purpose |
|---|---|---|
| **Language** | Kotlin 2.0+ | Modern type-safe Android development |
| **UI Framework** | Jetpack Compose + Material 3 | Declarative UI, smooth card animations, and responsive layout |
| **Architecture** | MVVM + Clean Architecture (Domain, Data, UI) | Decoupled UI logic, predictable unidirectional data flow |
| **Dependency Injection** | Hilt / Koin (or manual DI for lightweight setup) | Dependency graph management |
| **Networking** | Retrofit 2 + OkHttp 4 + Kotlin Serialization | REST API communication (`api.masterummy.com`) |
| **Realtime Gateway** | OkHttp WebSocket + Kotlinx Coroutines Flow | Real-time card gameplay events (`play.masterummy.com`) |
| **Local Database** | Room Database | Local player profile, room tiers, transaction history, offline card stats |
| **Local Key-Value** | Jetpack DataStore Preferences | User session tokens, sound preferences, vibration settings |
| **Asynchronous** | Kotlin Coroutines & StateFlow | Reactive state management |
| **Graphics & Cards** | Custom Compose Canvas + Vector Graphics | 60/120 FPS card drag-and-drop, fan-out, dealing animations |

---

## 2. Target Project Folder Structure

```
rebuilt-app/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/master/rummy/
│       │   │   ├── RummyApplication.kt
│       │   │   ├── core/
│       │   │   │   ├── engine/                # 13-Card Indian Rummy Rules Engine
│       │   │   │   │   ├── Card.kt            # Suit, Rank, Card Model
│       │   │   │   │   ├── CardGroup.kt       # Pure Seq, Impure Seq, Set, Invalid
│       │   │   │   │   ├── RummyValidator.kt  # Score calculation, win evaluation
│       │   │   │   │   └── BotPlayer.kt       # Local AI opponent for practice
│       │   │   │   ├── network/               # Retrofit APIs & WebSockets
│       │   │   │   │   ├── ApiService.kt
│       │   │   │   │   ├── WebSocketManager.kt
│       │   │   │   │   └── models/            # DTOs, Handshakes, Action packets
│       │   │   │   └── database/              # Room DB
│       │   │   │       ├── AppDatabase.kt
│       │   │   │       ├── entity/            # UserEntity, RoomEntity, GameHistoryEntity
│       │   │   │       └── dao/               # RoomDaos
│       │   │   ├── data/                      # Repositories & Data Sources
│       │   │   │   ├── AuthRepository.kt
│       │   │   │   ├── GameRepository.kt
│       │   │   │   └── WalletRepository.kt
│       │   │   └── ui/                        # Jetpack Compose Screens & ViewModels
│       │   │       ├── theme/                 # Color.kt (Felt Green, Gold), Type.kt, Theme.kt
│       │   │       ├── navigation/            # NavHost, Screen Routes
│       │   │       ├── components/            # PlayingCardView, TimerRing, ChipsView
│       │   │       ├── splash/                # SplashScreen, SplashViewModel
│       │   │       ├── auth/                  # LoginScreen, PhoneOtpScreen, AuthViewModel
│       │   │       ├── lobby/                 # LobbyScreen, RoomCard, LobbyViewModel
│       │   │       ├── table/                 # GameTableScreen, TableViewModel
│       │   │       │   ├── components/        # PlayerSeat, CardHandFan, DiscardPile, DrawDeck
│       │   │       │   └── dialogs/           # DeclareDialog, ScoreSettlementDialog
│       │   │       ├── wallet/                # AddCashScreen, WithdrawScreen, WalletViewModel
│       │   │       └── profile/               # ProfileScreen, KycScreen
│       │   └── res/
│       │       ├── drawable/                  # Vector cards (suits, back texture, chip assets)
│       │       ├── values/                    # strings.xml, colors.xml
│       │       └── mipmap/                    # ic_launcher.png
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 3. Screen-by-Screen Implementation Task List

### Phase 3.1: Foundation & Project Setup
- [ ] Initialize Android Studio Gradle project in `./rebuilt-app` with Compose BOM & Material 3.
- [ ] Configure `minSdk = 24`, `targetSdk = 34+`, JVM toolchain Java 17/21.
- [ ] Set up colors, typography (Roboto), and custom theme matching original green/gold palette.
- [ ] Create vector assets for card suits (♠, ♥, ♦, ♣), card back patterns, and casino chips.

### Phase 3.2: 13-Card Indian Rummy Domain Engine (Pure Kotlin)
- [ ] Implement `Card(suit, rank, isWildJoker, isPrintedJoker)`.
- [ ] Implement group validation:
  - **Pure Sequence:** 3+ consecutive cards of same suit without joker (unless joker used in its natural rank).
  - **Impure Sequence:** 3+ consecutive cards of same suit using jokers.
  - **Set:** 3 or 4 cards of same rank in different suits.
- [ ] Implement hand validator: Valid show requires at least 2 sequences, with at least 1 being a Pure Sequence.
- [ ] Implement points scoring: Number cards = face value (2-10), Face cards (J, Q, K, A) = 10 points. Maximum penalty = 80 points. Drop = 20 / Middle Drop = 40.

### Phase 3.3: Splash & Onboarding Flow
- [ ] Implement `SplashScreen` with branding animation and session verification.
- [ ] Implement `LoginScreen` supporting Mobile OTP input and Guest Quick-Play.
- [ ] Implement `PhoneOtpDialog` with countdown resend timer.

### Phase 3.4: Lobby & Game Mode Selection
- [ ] Implement `LobbyScreen` header: Avatar, Name, Cash Balance, Practice Chips, VIP badge.
- [ ] Game Mode Cards:
  - **Points Rummy:** Tier list (₹0.05/pt to ₹50/pt), Min entry fee, Active players count.
  - **Pool Rummy:** 101 Pool and 201 Pool selectors.
  - **Deals Rummy:** 2 Deals and 3 Deals selectors.
- [ ] Bottom Navigation Bar: Lobby, Add Cash, Daily Rewards, Profile, Help.

### Phase 3.5: Interactive Game Table (Core Gameplay)
- [ ] Implement `GameTableScreen` in Portrait orientation with felt table background.
- [ ] Opponent bar: Profile image, 13 face-down card count, turn timer progress ring (30s).
- [ ] Deck area:
  - Closed Deck (touch to draw face-down card).
  - Open Discard Pile (touch to draw top discarded card).
  - Wild Joker display indicator.
- [ ] Player Hand area:
  - Drag-and-drop card reordering.
  - Multi-select card grouping into melds/slots.
  - "Sort / Auto-Group" button.
  - "Discard" button and "Declare" button.
- [ ] Turn management state machine (`WAITING`, `DEAL`, `PICK_CARD`, `DISCARD_CARD`, `DECLARE`, `SETTLEMENT`).
- [ ] Round Settlement Dialog: Breakdown of scores, player hands, penalties, and winnings.

### Phase 3.6: Wallet, Cashier & Profile
- [ ] Implement `WalletScreen`: Deposit options (UPI, Net Banking), transaction history.
- [ ] Implement `ProfileScreen`: KYC status, stats (games played, win rate), bank details.
- [ ] Implement offline bot mode for instant testing without server connection.

### Phase 3.7: Build Verification
- [ ] Build release and debug APKs using `./gradlew assembleDebug`.
- [ ] Verify zero compilation errors and test gameplay flow.
