
/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO(val connection:String)  {

    fun add(gameID: Int, date: String, winners: Array<Int>, losers: Array<Int>, traitors: Array<Int>): Int {
        return 0

    }

    fun get(from:Int = 0, numOf:Int = 0): Array<Session> {
        val sessions = arrayOf(Session(id=0,date="asd",gameID = 1,winners = arrayOf(1), losers = arrayOf(2), traitors = arrayOf(2)))
        return sessions
    }

    fun remove(id: Int): Boolean {
        return false
    }

}

data class Session(val id: Int, val date: String, val gameID: Int, val winners: Array<Int>, val losers: Array<Int>, val traitors: Array<Int>)