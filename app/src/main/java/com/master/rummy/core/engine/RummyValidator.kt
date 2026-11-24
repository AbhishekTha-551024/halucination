package com.master.rummy.core.engine

object RummyValidator {

    /**
     * Checks if a group of cards forms a Pure Sequence:
     * - 3 or more cards of the exact same suit
     * - In consecutive rank order
     * - No wild joker substituting a card (a wild joker can only be used in its natural rank & suit)
     * - No duplicates
     */
    fun isPureSequence(cards: List<Card>): Boolean {
        if (cards.size < 3) return false
        val suit = cards.first().suit
        if (cards.any { it.suit != suit || it.isPrintedJoker }) return false

        val ranks = cards.map { it.rank.value }.distinct()
        if (ranks.size != cards.size) return false // Duplicates not allowed

        val sorted = ranks.sorted()
        // Check consecutive normal: e.g. 4, 5, 6 or 10, 11, 12, 13
        var isConsecutive = true
        for (i in 0 until sorted.size - 1) {
            if (sorted[i + 1] != sorted[i] + 1) {
                isConsecutive = false
                break
            }
        }
        if (isConsecutive) return true

        // Check Ace high: Q, K, A -> 11, 12, 13, 1 (mapped to 11, 12, 13, 14)
        if (sorted.contains(1)) {
            val aceHigh = sorted.map { if (it == 1) 14 else it }.sorted()
            var isAceHighConsecutive = true
            for (i in 0 until aceHigh.size - 1) {
                if (aceHigh[i + 1] != aceHigh[i] + 1) {
                    isAceHighConsecutive = false
                    break
                }
            }
            if (isAceHighConsecutive) return true
        }

        return false
    }

    /**
     * Checks if a group forms an Impure Sequence:
     * - 3 or more cards of same suit where Jokers can fill in missing spots
     */
    fun isImpureSequence(cards: List<Card>): Boolean {
        if (cards.size < 3) return false
        val nonJokers = cards.filter { !it.isWildJoker && !it.isPrintedJoker }
        val jokersCount = cards.size - nonJokers.size

        if (jokersCount == 0) return isPureSequence(cards)
        if (nonJokers.isEmpty()) return false // All jokers cannot form sequence alone

        val targetSuit = nonJokers.first().suit
        if (nonJokers.any { it.suit != targetSuit }) return false

        val nonJokerRanks = nonJokers.map { it.rank.value }
        if (nonJokerRanks.distinct().size != nonJokerRanks.size) return false // Duplicates in same sequence

        // Check normal ordering with gap filling
        if (canFormConsecutiveWithJokers(nonJokerRanks.sorted(), jokersCount, cards.size)) return true

        // Check Ace-high ordering: Q, K, A with jokers
        if (nonJokerRanks.contains(1)) {
            val aceHighRanks = nonJokerRanks.map { if (it == 1) 14 else it }.sorted()
            if (canFormConsecutiveWithJokers(aceHighRanks, jokersCount, cards.size)) return true
        }

        return false
    }

    private fun canFormConsecutiveWithJokers(sortedRanks: List<Int>, jokers: Int, totalSize: Int): Boolean {
        var missingGaps = 0
        for (i in 0 until sortedRanks.size - 1) {
            val diff = sortedRanks[i + 1] - sortedRanks[i] - 1
            if (diff < 0) return false
            missingGaps += diff
        }
        if (missingGaps > jokers) return false
        val span = sortedRanks.last() - sortedRanks.first() + 1
        return span <= totalSize && totalSize <= 14
    }

    /**
     * Checks if a group forms a Set:
     * - 3 or 4 cards of the exact same rank
     * - All non-jokers must have DIFFERENT suits
     */
    fun isSet(cards: List<Card>): Boolean {
        if (cards.size !in 3..4) return false
        val nonJokers = cards.filter { !it.isWildJoker && !it.isPrintedJoker }
        if (nonJokers.isEmpty()) return false

        val targetRank = nonJokers.first().rank
        if (nonJokers.any { it.rank != targetRank }) return false

        val suits = nonJokers.map { it.suit }
        return suits.distinct().size == suits.size // No duplicate suits in set
    }

    fun classifyGroup(cards: List<Card>): CardGroup {
        if (cards.isEmpty()) return CardGroup(cards, GroupType.INVALID, 0)
        val type = when {
            isPureSequence(cards) -> GroupType.PURE_SEQUENCE
            isImpureSequence(cards) -> GroupType.IMPURE_SEQUENCE
            isSet(cards) -> GroupType.SET
            else -> GroupType.INVALID
        }
        val points = if (type.isValid) 0 else cards.sumOf { it.points }
        return CardGroup(cards, type, points)
    }

    /**
     * Complete validation for a 13-card hand declaration:
     * - Must have at least 1 Pure Sequence
     * - Must have at least 1 second Sequence (pure or impure)
     * - All groups must be valid (Sequences or Sets)
     *
     * Returns total penalty score (0 if valid, max 80 if invalid).
     */
    fun validateDeclaration(groups: List<List<Card>>): DeclarationResult {
        val classified = groups.map { classifyGroup(it) }
        val pureCount = classified.count { it.type == GroupType.PURE_SEQUENCE }
        val seqCount = classified.count { it.type == GroupType.PURE_SEQUENCE || it.type == GroupType.IMPURE_SEQUENCE }
        val allValid = classified.all { it.type.isValid } && classified.sumOf { it.cards.size } == 13

        val isValidShow = pureCount >= 1 && seqCount >= 2 && allValid

        val penaltyPoints = if (isValidShow) {
            0
        } else {
            // Calculate deadwood
            when {
                pureCount == 0 -> {
                    // No pure sequence: all unmatched non-joker cards count
                    minOf(80, classified.flatMap { it.cards }.filter { !it.isWildJoker && !it.isPrintedJoker }.sumOf { it.points })
                }
                seqCount < 2 -> {
                    // Pure sequence exists, but no second sequence:
                    // Pure sequence is exempt, all other groups count
                    val nonPure = classified.filter { it.type != GroupType.PURE_SEQUENCE }
                    minOf(80, nonPure.flatMap { it.cards }.filter { !it.isWildJoker && !it.isPrintedJoker }.sumOf { it.points })
                }
                else -> {
                    // Both sequences exist: only invalid groups count
                    val invalid = classified.filter { !it.type.isValid }
                    minOf(80, invalid.flatMap { it.cards }.filter { !it.isWildJoker && !it.isPrintedJoker }.sumOf { it.points })
                }
            }
        }

        return DeclarationResult(
            isValid = isValidShow,
            penaltyPoints = penaltyPoints,
            groups = classified,
            hasPureSequence = pureCount >= 1,
            hasSecondSequence = seqCount >= 2
        )
    }
}

data class DeclarationResult(
    val isValid: Boolean,
    val penaltyPoints: Int,
    val groups: List<CardGroup>,
    val hasPureSequence: Boolean,
    val hasSecondSequence: Boolean
)
