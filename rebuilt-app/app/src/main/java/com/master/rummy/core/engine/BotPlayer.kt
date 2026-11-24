package com.master.rummy.core.engine

class BotPlayer(
    val name: String = "Rajesh_Bot",
    val avatarId: Int = 1
) {
    var hand: MutableList<Card> = mutableListOf()

    fun reset() {
        hand.clear()
    }

    /**
     * Bot turn logic:
     * 1. Decide whether to pick from open discard pile or closed deck
     * 2. Add picked card
     * 3. Discard highest deadwood card
     */
    fun takeTurn(openCard: Card, drawFromDeck: () -> Card): Pair<Card, Boolean> {
        // Evaluate if open card helps complete a sequence
        val potentialHand = hand + openCard
        val currentPenalty = RummyValidator.validateDeclaration(AutoGrouper.autoGroup(hand)).penaltyPoints
        val newPenalty = RummyValidator.validateDeclaration(AutoGrouper.autoGroup(potentialHand)).penaltyPoints

        val pickedCard = if (newPenalty < currentPenalty || openCard.isWildJoker) {
            openCard
        } else {
            drawFromDeck()
        }
        hand.add(pickedCard)

        // Re-group and find best card to discard (highest point deadwood)
        val grouped = AutoGrouper.autoGroup(hand)
        val invalidCards = grouped.filter { !it.let(RummyValidator::classifyGroup).type.isValid }
            .flatMap { it }
            .filter { !it.isWildJoker && !it.isPrintedJoker }

        val discardCard = if (invalidCards.isNotEmpty()) {
            invalidCards.maxByOrNull { it.points } ?: hand.last()
        } else {
            // Pick any non-joker card
            hand.filter { !it.isWildJoker && !it.isPrintedJoker }.maxByOrNull { it.points } ?: hand.last()
        }

        hand.remove(discardCard)

        val finalCheck = RummyValidator.validateDeclaration(AutoGrouper.autoGroup(hand))
        return Pair(discardCard, finalCheck.isValid)
    }
}
