package com.master.rummy

import com.master.rummy.core.engine.Card
import com.master.rummy.core.engine.GroupType
import com.master.rummy.core.engine.Rank
import com.master.rummy.core.engine.RummyValidator
import com.master.rummy.core.engine.Suit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RummyValidatorTest {

    @Test
    fun testPureSequence() {
        val pureSeq = listOf(
            Card(1, Suit.HEARTS, Rank.FOUR),
            Card(2, Suit.HEARTS, Rank.FIVE),
            Card(3, Suit.HEARTS, Rank.SIX)
        )
        assertTrue("4♥, 5♥, 6♥ must be pure sequence", RummyValidator.isPureSequence(pureSeq))
    }

    @Test
    fun testPureSequenceAceHigh() {
        val aceHighSeq = listOf(
            Card(1, Suit.SPADES, Rank.QUEEN),
            Card(2, Suit.SPADES, Rank.KING),
            Card(3, Suit.SPADES, Rank.ACE)
        )
        assertTrue("Q♠, K♠, A♠ must be pure sequence", RummyValidator.isPureSequence(aceHighSeq))
    }

    @Test
    fun testImpureSequenceWithJoker() {
        val impureSeq = listOf(
            Card(1, Suit.CLUBS, Rank.SEVEN),
            Card(2, Suit.CLUBS, Rank.NINE, isWildJoker = true), // Wild Joker substituting 8♣
            Card(3, Suit.CLUBS, Rank.NINE)
        )
        assertTrue("7♣, Joker, 9♣ must be impure sequence", RummyValidator.isImpureSequence(impureSeq))
    }

    @Test
    fun testValidSet() {
        val set = listOf(
            Card(1, Suit.SPADES, Rank.KING),
            Card(2, Suit.HEARTS, Rank.KING),
            Card(3, Suit.CLUBS, Rank.KING)
        )
        assertTrue("K♠, K♥, K♣ must be valid set", RummyValidator.isSet(set))

        val invalidSetDuplicateSuits = listOf(
            Card(1, Suit.SPADES, Rank.KING),
            Card(2, Suit.SPADES, Rank.KING),
            Card(3, Suit.CLUBS, Rank.KING)
        )
        assertFalse("Set with duplicate suits must be invalid", RummyValidator.isSet(invalidSetDuplicateSuits))
    }

    @Test
    fun test13CardDeclarationValidation() {
        // Group 1: Pure sequence (4 cards)
        val pure = listOf(
            Card(1, Suit.HEARTS, Rank.TWO),
            Card(2, Suit.HEARTS, Rank.THREE),
            Card(3, Suit.HEARTS, Rank.FOUR),
            Card(4, Suit.HEARTS, Rank.FIVE)
        )
        // Group 2: Impure sequence (3 cards)
        val impure = listOf(
            Card(5, Suit.SPADES, Rank.TEN),
            Card(6, Suit.SPADES, Rank.JACK),
            Card(7, Suit.CLUBS, Rank.TWO, isWildJoker = true) // Joker substituting Q♠
        )
        // Group 3: Set (3 cards)
        val set1 = listOf(
            Card(8, Suit.DIAMONDS, Rank.SEVEN),
            Card(9, Suit.CLUBS, Rank.SEVEN),
            Card(10, Suit.SPADES, Rank.SEVEN)
        )
        // Group 4: Set (3 cards)
        val set2 = listOf(
            Card(11, Suit.HEARTS, Rank.ACE),
            Card(12, Suit.DIAMONDS, Rank.ACE),
            Card(13, Suit.CLUBS, Rank.ACE)
        )

        val result = RummyValidator.validateDeclaration(listOf(pure, impure, set1, set2))
        assertTrue("Hand with pure seq + second seq + sets must be valid show", result.isValid)
        assertEquals("Penalty points for valid show must be 0", 0, result.penaltyPoints)
    }
}
