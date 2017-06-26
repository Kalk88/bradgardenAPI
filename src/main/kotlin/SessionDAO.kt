import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO  {

    fun add(gameID: Int, date: String, winners: Array<Int>, losers: Array<Int>, traitors: Array<Int>): Int {
       val sessionID: Int
        try {
            val con = openConnection()
            val insertSession  = "insert into game_session (game, session_date) values ?, ? returning session_id"
            val insertWinner = "insert into winner values ?, ?"
            val insertLoser = "insert into loser values ?, ?"
            val insertTraitor = "insert into traitor values ?, ?"
            var stmt = con.prepareStatement(insertSession)
            stmt.setInt(1, gameID)
            stmt.setString(2, date)
            stmt.executeQuery()
            stmt.resultSet.next()
            sessionID = stmt.resultSet.getInt(1)

            for (winner in winners) {
                stmt = con.prepareStatement(insertWinner)
                stmt.setInt(1, sessionID)
                stmt.setInt(2, winner)
                stmt.executeQuery()
            }
            for (loser in losers) {
                stmt = con.prepareStatement(insertLoser)
                stmt.setInt(1, sessionID)
                stmt.setInt(2, loser)
                stmt.executeQuery()
            }
            if(traitors.size > 0) {
                for (traitor in traitors) {
                    stmt = con.prepareStatement(insertTraitor)
                    stmt.setInt(1, sessionID)
                    stmt.setInt(2, traitor)
                    stmt.executeQuery()
                }
            }
        } catch (e: Exception) {
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
            stmt.close()
        } catch (e: Exception) {
            throw APIException("${e.message}")
        }
        return sessions
    }

    fun remove(id: Int): Boolean {
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("delete from game_session where session_id = ?")
            stmt.setInt(1, id)
            stmt.execute()
            stmt.close()
            return true
        } catch (e: Exception) {
            throw APIException("${e.message}")
        }
        return false
    }
}

data class lightSession(val id: Int, val date: String, val gameID: Int)
data class Session(val id: Int, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)