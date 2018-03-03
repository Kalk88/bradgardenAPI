/**
 * All data classes for this project.
 */

/**
 * Represents a member in brädgården
 * Id, first and last name is set once, other attributes can be mutated.
 * @param id  Int
 * @param firstName String
 * @param lastName String
 * @param wins Int
 * @param winRatio Double
 * @param losses Int
 * @param timesTraitor Int
 * @param gamesPlayed Int
 */
data class Member(val id: Int = -1, val firstName: String, val lastName: String,
                  var wins: Int, var winRatio: Double, var losses: Int,
                  var timesTraitor: Int, var gamesPlayed: Int) {
    init {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) {"Invalid name."}
        require(firstName.length > 1) {"$firstName is invalid, must be at least 2 characters."}
        require(lastName.length > 1) {"$lastName is invalid, Name must be at least 2 characters."}
    }
}

/**
 * Represents a game owned by brädgården
 */
data class Game(val id: Int = -1, val name: String, val maxNumOfPlayers: Int, val traitor: Boolean, val coop: Boolean) {
    init {
        require(maxNumOfPlayers > 0) {"Number of players must be greater than 0"}
        require(name.length > 1) {"${name} is invalid, must be at least 2 characters."}
    }
}

/**
 * Represents one completed playthrough of a game, if several games or rounds of games were played during meeting
 * each round is a separate session.
 */
data class Session(val id: Int = -1, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>){
    init {
       // val dateformat = Regex("""\s{4}-\s{2}-\s{2}T\s{2}:\s{2}:\s{2}\+\s{2}:\s{2}""")
       // require(date.matches(dateformat)) {"Dateformat should conform to ISO 8601 standard e.g. 1997-07-16T19:20:30.45+01:00"}
    }
}