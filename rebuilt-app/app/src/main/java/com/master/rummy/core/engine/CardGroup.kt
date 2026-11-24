package com.master.rummy.core.engine

enum class GroupType(val label: String, val isValid: Boolean) {
    PURE_SEQUENCE("Pure Sequence", true),
    IMPURE_SEQUENCE("Impure Sequence", true),
    SET("Set", true),
    INVALID("Invalid Group", false)
}

data class CardGroup(
    val cards: List<Card>,
    val type: GroupType,
    val points: Int
)
