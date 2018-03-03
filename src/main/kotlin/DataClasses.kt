/**
 * All data classes for this project.
 */

data class Member(val id: Int = -1, val firstName: String, val lastName: String,
                  val wins: Int, val winRatio: Double, val losses: Int,
                  val timesTraitor: Int, val gamesPlayed: Int) {
    init {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) {"Invalid name."}
        require(firstName.length > 1) {"$firstName is invalid, must be at least 2 characters."}
        require(lastName.length > 1) {"$lastName is invalid, Name must be at least 2 characters."}
    }
}

data class Game(val id: Int = -1, val name: String, val maxNumOfPlayers: Int, val traitor: Boolean, val coop: Boolean) {
    init {
        require(maxNumOfPlayers > 0) {"Number of players must be greater than 0"}
        require(name.length > 1) {"${name} is invalid, must be at least 2 characters."}
    }
}

data class Session(val id: Int = -1, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)