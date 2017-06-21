import java.util.*

/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO(val connection:String)  {

    fun add(gameID: Int, date: String, winners: Array<Int>, losers: Array<Int>, traitors: Array<Int>): Int {

    }

    fun get(from:Int = 0, numOf:Int = 0): Array<Session> {

    }

    fun remove(id: Int): Boolean {}

}

data class Session(val id: Int, val date: String, val gameID: Int, val winners: Array<Int>, val losers: Array<Int>, val traitors: Array<Int>)