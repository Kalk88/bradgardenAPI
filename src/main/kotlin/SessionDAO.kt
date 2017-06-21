import java.util.*

/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO(val connection:String)  {

    fun addSession(gameID: Int, date: String, winners: Array<Int>, losers: Array<Int>, traitors: Array<Int>): Int {

    }

    fun getSessions(from:Int = 0, numOf:Int = 0): Array<Session> {

    }

    fun removeSession(id: Int): Boolean {}

}

data class Session(val id: Int, val date: String, val gameID: Int, val winners: Array<Int>, val losers: Array<Int>, val traitors: Array<Int>)