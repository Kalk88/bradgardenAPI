import org.junit.Test
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertTrue;

class VacTests {

    @Test fun should_return_fasle_when_member_is_winner_and_loser() {
        val winners = listOf(1, 2)
        val losers = listOf(1,3,5)
        assertTrue(winnersInLosers(winners, losers))
    }

    @Test fun should_return_true_when_members_is_not_in_both() {
        val winners = listOf(1, 2)
        val losers = listOf(3,5)
        assertFalse(winnersInLosers(winners, losers))
    }

    @Test fun should_return_fasle_when_traitor_is_not_in_winner_or_loser() {
        val winners = listOf(1, 2)
        val losers = listOf(3,5)
        val traitors = listOf(3,7)
        assertFalse(traitorIsInWinnersOrLosers(traitors,winners, losers))
    }

    @Test fun should_return_true_when_traitor_is_in_one_of_winners_or_losers() {
        val winners = listOf(1, 2)
        val losers = listOf(3,5)
        val traitors = listOf(3)
        assertTrue(traitorIsInWinnersOrLosers(traitors,winners, losers))

        val traitors2 = listOf(1)
        assertTrue(traitorIsInWinnersOrLosers(traitors2, winners, losers))
    }
}

