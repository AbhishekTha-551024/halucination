package com.master.rummy.core.engine

object AutoGrouper {

    /**
     * Groups 13 cards into standard player slots (usually 4 melds)
     * prioritizing pure sequences, then sets, then impure sequences.
     */
    fun autoGroup(cards: List<Card>): List<List<Card>> {
        if (cards.isEmpty()) return emptyList()

        val remaining = cards.toMutableList()
        val groups = mutableListOf<List<Card>>()

        // Step 1: Extract Pure Sequences
        for (suit in Suit.entries) {
            val suitCards = remaining.filter { it.suit == suit && !it.isPrintedJoker && !it.isWildJoker }
                .sortedBy { it.rank.value }

            if (suitCards.size >= 3) {
                // Find consecutive runs of 3 or more
                val currentRun = mutableListOf<Card>()
                for (card in suitCards) {
                    if (currentRun.isEmpty()) {
                        currentRun.add(card)
                    } else if (card.rank.value == currentRun.last().rank.value + 1) {
                        currentRun.add(card)
                    } else if (card.rank.value != currentRun.last().rank.value) {
                        if (currentRun.size >= 3) {
                            groups.add(currentRun.toList())
                            remaining.removeAll(currentRun)
                        }
                        currentRun.clear()
                        currentRun.add(card)
                    }
                }
                if (currentRun.size >= 3) {
                    groups.add(currentRun.toList())
                    remaining.removeAll(currentRun)
                }
            }
        }

        // Step 2: Extract Sets from remaining non-jokers
        val nonJokers = remaining.filter { !it.isPrintedJoker && !it.isWildJoker }
        val byRank = nonJokers.groupBy { it.rank }
        for ((_, rankCards) in byRank) {
            val distinctSuits = rankCards.distinctBy { it.suit }
            if (distinctSuits.size >= 3) {
                groups.add(distinctSuits)
                remaining.removeAll(distinctSuits)
            }
        }

        // Step 3: Use Jokers with remaining cards to form impure sequences or sets
        val jokers = remaining.filter { it.isPrintedJoker || it.isWildJoker }.toMutableList()
        remaining.removeAll(jokers)

        // Group remaining non-jokers by suit
        val byRemainingSuit = remaining.groupBy { it.suit }
        for ((_, suitCards) in byRemainingSuit) {
            if (suitCards.size >= 2 && jokers.isNotEmpty()) {
                val needJoker = mutableListOf<Card>().apply {
                    addAll(suitCards.sortedBy { it.rank.value })
                    add(jokers.removeAt(0))
                }
                groups.add(needJoker)
                remaining.removeAll(suitCards)
            }
        }

        // Add any remaining cards and jokers
        if (remaining.isNotEmpty() || jokers.isNotEmpty()) {
            val leftover = mutableListOf<Card>().apply {
                addAll(remaining)
                addAll(jokers)
            }
            // Split into manageable chunks if too large (max 5-6 cards per slot)
            if (leftover.size > 5) {
                groups.add(leftover.take(leftover.size / 2))
                groups.add(leftover.drop(leftover.size / 2))
            } else {
                groups.add(leftover)
            }
        }

        return groups.ifEmpty { listOf(cards) }
    }
}
