package com.master.rummy.core.engine

enum class Suit(val symbol: String, val isRed: Boolean) {
    SPADES("♠", false),
    HEARTS("♥", true),
    CLUBS("♣", false),
    DIAMONDS("♦", true)
}

enum class Rank(val value: Int, val label: String, val points: Int) {
    ACE(1, "A", 10),
    TWO(2, "2", 2),
    THREE(3, "3", 3),
    FOUR(4, "4", 4),
    FIVE(5, "5", 5),
    SIX(6, "6", 6),
    SEVEN(7, "7", 7),
    EIGHT(8, "8", 8),
    NINE(9, "9", 9),
    TEN(10, "10", 10),
    JACK(11, "J", 10),
    QUEEN(12, "Q", 10),
    KING(13, "K", 10);

    companion object {
        fun fromValue(value: Int): Rank = entries.firstOrNull { it.value == value } ?: ACE
    }
}

data class Card(
    val id: Int,
    val suit: Suit,
    val rank: Rank,
    val isWildJoker: Boolean = false,
    val isPrintedJoker: Boolean = false
) {
    val points: Int
        get() = if (isWildJoker || isPrintedJoker) 0 else rank.points

    val displayName: String
        get() = if (isPrintedJoker) "🃏 JOKER" else "${rank.label}${suit.symbol}"
}
