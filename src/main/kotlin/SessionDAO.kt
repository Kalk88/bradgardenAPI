import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO  {

    fun add(gameID: Int, date: String, winners: List<Int>, losers: List<Int>, traitors: List<Int>): Int {
       val sessionID: Int
        try {
            val con = openConnection()
            val insertSession  = "insert into game_session (game, session_date) values (?, ?) returning session_id"
            val insertWinner = "insert into winner values (?, ?)"
            val insertLoser = "insert into loser  values (?, ?)"
            val insertTraitor = "insert into traitor values (?, ?)"
            var win : PreparedStatement
            var lose: PreparedStatement
            var trait : PreparedStatement
            var stmt = con.prepareStatement(insertSession)
            stmt.setInt(1, gameID)
            stmt.setString(2, date)
            stmt.executeQuery()
            stmt.resultSet.next()
            sessionID = stmt.resultSet.getInt(1)
            print(sessionID)

            for (winner in winners) {
                win = con.prepareStatement(insertWinner)
                win.setInt(1, sessionID)
                win.setInt(2, winner)
                win.execute()
            }

            for (loser in losers) {
               lose = con.prepareStatement(insertLoser)
                lose.setInt(1, sessionID)
                lose.setInt(2, loser)
                lose.execute()
            }
            if(traitors.isNotEmpty()) {
                for (traitor in traitors) {
                    trait = con.prepareStatement(insertTraitor)
                    trait.setInt(1, sessionID)
                    trait.setInt(2, traitor)
                    trait.execute()
                }
            }
            con.close()
        } catch (e: Exception) {
            e.printStackTrace()
            throw APIException("${e.message}")
        }
        return sessionID
    }

    fun get(limit:Int = 100, offset:Int = 0): ArrayList<lightSession> {
        val sessions = ArrayList<lightSession>()
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("select * from game_session limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            val rs: ResultSet
            rs = stmt.executeQuery()
            while (rs.next()) {
                sessions.add(lightSession(id = rs.getInt(1), gameID = rs.getInt(2), date = rs.getString(3)))
            }
            con.close()
        } catch (e: Exception) {
            throw APIException("${e.message}")
        }
        return sessions
    }

    fun delete(id: Int): Boolean {
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("delete from game_session where session_id = ?")
            stmt.setInt(1, id)
            stmt.execute()
            con.close()
            return true
        } catch (e: Exception) {
            throw APIException("${e.message}")
        }
        return false
    }
}

data class lightSession(val id: Int, val date: String, val gameID: Int)
data class addSession(val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)
data class Session(val id: Int, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)