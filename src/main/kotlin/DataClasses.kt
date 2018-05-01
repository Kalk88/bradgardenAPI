
import java.text.SimpleDateFormat
import java.util.*


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
data class Member(var id: Int = -1, var firstName: String, var lastName: String,
                  var wins: Int, var winRatio: Double, var losses: Int,
                  var timesTraitor: Int, var gamesPlayed: Int) {
    init {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.first().isWhitespace())
        require(!lastName.first().isWhitespace())
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) { "Invalid name." }
        require(firstName.length > 1) { "$firstName is invalid, must be at least 2 characters." }
        require(lastName.length > 1) { "$lastName is invalid, Name must be at least 2 characters." }
        firstName = transformTitleCase(firstName)
        lastName = transformTitleCase(lastName)
    }
}
/**
 * Represents a game owned by brädgården
 */
data class Game(var id: Int? = -1, var name: String, val maxNumOfPlayers: Int, val traitor: Boolean, val coop: Boolean) {
    init {
        require(maxNumOfPlayers > 0) {"Number of players must be greater than 0"}
        require(name.length > 1) {"${name} is invalid, must be at least 2 characters."}
        name = transformTitleCase(name)
    }
}

/**
 * Represents one completed playthrough of a game, if several games or rounds of games were played during meeting
 * each round is a separate session.
 */
data class Session(var id: Int = -1, var date: String?, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>) {
    init {
        if(date == null) {
            val tz = TimeZone.getTimeZone("Europe/Copenhagen")
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ")
            df.timeZone = tz
            date = df.format(Date())
        }
    }
}

fun transformTitleCase( text:String): String {
    var sb = StringBuilder()
    sb.append(text.first().toUpperCase())
    for (index in 1 until text.length) {
        if(text[index-1].isWhitespace())
            sb.append(text[index].toUpperCase())
        else sb.append(text[index].toLowerCase())
    }

    return sb.toString()
}

