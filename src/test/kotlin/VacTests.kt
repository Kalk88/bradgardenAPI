import org.junit.Test

class VacTests {

    @Test fun should_return_fasle_when_member_is_winner_and_loser() {
        val winners = listOf(1, 2)
        val losers = listOf(1,3,5)
        assert(winnersInLosers(winners, losers))
    }

    @Test fun should_return_true_when_members_is_not_in_both() {
        val winners = listOf(1, 2)
        val losers = listOf(3,5)
        assert(!winnersInLosers(winners, losers))
    }

    @Test fun should_return_fasle_when_traitor_is_not_in_winner_or_loser() {
        val winners = listOf(1, 2)
        val losers = listOf(3,5)
        val traitors = listOf(3,7)
        assert(!traitorIsInWinnersOrLosers(traitors,winners, losers))
    }

    @Test fun should_return_true_when_traitor_is_in_one_of_winners_or_losers() {
        val winners = listOf(1, 2)
        val losers = listOf(3,5)
        val traitors = listOf(3)
        assert(traitorIsInWinnersOrLosers(traitors,winners, losers))

        val traitors2 = listOf(1)
        assert(traitorIsInWinnersOrLosers(traitors2, winners, losers))
    }
}

