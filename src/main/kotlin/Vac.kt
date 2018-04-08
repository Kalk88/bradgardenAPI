
fun winnersInLosers(winners:List<Int>, losers:List<Int>): Boolean {
    winners.forEach { aId ->
        losers.forEach {
            if(aId == it)
                return true
        }
    }
    return false
}

fun traitorIsInWinnersOrLosers(traitors: List<Int>, winners:List<Int>, losers:List<Int>): Boolean {
    var inWinner: List<Int>
    var inLoser: List<Int>

    traitors.forEach { tId ->
        inWinner = winners.filter { it == tId }
        inLoser = losers.filter { it == tId }
        if(inWinner.isEmpty() && inLoser.isEmpty())
                return false
    }

    return true
}