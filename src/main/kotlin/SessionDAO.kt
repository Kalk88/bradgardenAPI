import mu.KLogging
import org.apache.commons.dbutils.DbUtils
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.ArrayList
/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO {
    companion object: KLogging()

    fun add(gameID: Int, date: String, winners: List<Int>, losers: List<Int>, traitors: List<Int>): Int {
       val sessionID: Int
        val con = DBConnection.instance.open()
        try {
            val insertSession  = "insert into game_session (game, session_date) values (?, ?) returning session_id"
            val insertWinner = "insert into winner values (?, ?)"
            val insertLoser = "insert into loser  values (?, ?)"
            val insertTraitor = "insert into traitor values (?, ?)"
            var win: PreparedStatement
            var lose: PreparedStatement
            var traitor: PreparedStatement
            var session = con.prepareStatement(insertSession)
            session.setInt(1, gameID)
            session.setString(2, date)
            session.executeQuery()
            session.resultSet.next()
            sessionID = session.resultSet.getInt(1)
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
                for (t in traitors) {
                    traitor = con.prepareStatement(insertTraitor)
                    traitor.setInt(1, sessionID)
                    traitor.setInt(2, t)
                    traitor.execute()
                }
            }
        } catch (e: Exception) {
            logger.error("Error ADD ${e.message}")
            throw APIException("Could not add session")
        } finally {
            DbUtils.close(con)
        }
        return sessionID
    }

    fun get(limit:Int = 100, offset:Int = 0): ArrayList<lightSession> {
        val sessions = ArrayList<lightSession>()
        val con = DBConnection.instance.open()
        try {
            val stmt = con.prepareStatement("select * from game_session limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            val rs: ResultSet
            rs = stmt.executeQuery()
            while (rs.next()) {
                sessions.add(lightSession(id = rs.getInt(1), gameID = rs.getInt(2), date = rs.getString(3)))
            }
        } catch (e: Exception) {
            logger.error("Error GET ${e.message}")
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
        return sessions
    }

    fun getDetailed(id: Int): Session {
        val getSession =  "select * from game_session where session_id=?"
        val getWinners =  "select member from winner where game_session = ?"
        val getLosers =   "select member from loser where game_session = ?"
        val getTraitors = "select member from traitor where game_session = ?"
        var win: PreparedStatement
        var lose: PreparedStatement
        var traitor: PreparedStatement
        var sess: PreparedStatement
        val con = DBConnection.instance.open()
        try {
            sess = con.prepareStatement(getSession)
            sess.setInt(1, id)
            sess.executeQuery()
            sess.resultSet.next()
            win = con.prepareStatement(getWinners)
            win.setInt(1, id)
            win.executeQuery()
            val w = ArrayList<Int>()
            while (win.resultSet.next()) {
                w.add(win.resultSet.getInt(1))
            }
            lose = con.prepareStatement(getLosers)
            lose.setInt(1, id)
            lose.executeQuery()
            val l = ArrayList<Int>()
            while (lose.resultSet.next()) {
                l.add(lose.resultSet.getInt(1))
            }
            traitor = con.prepareStatement(getTraitors)
            traitor.setInt(1, id)
            traitor.executeQuery()
            val t = ArrayList<Int>()
            while (traitor.resultSet.next()) {
                t.add(traitor.resultSet.getInt(1))
            }
            return Session(id, gameID = sess.resultSet.getInt(2), date = sess.resultSet.getString(3),
                            winners = w, losers = l, traitors = t)
        } catch (e: Exception) {
            logger.error("Error GET DETAILED ${e.message}")
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
    }

    fun delete(id: Int): Boolean {
        val con = DBConnection.instance.open()
        try {
            val stmt = con.prepareStatement("delete from game_session where session_id = ?")
            stmt.setInt(1, id)
            stmt.execute()
            con.close()
            return true
        } catch (e: Exception) {
            logger.error("Error DELETE ${e.message}")
            throw APIException("Failed to delete $id")
        } finally {
            DbUtils.close(con)
        }
    }
}
data class lightSession(val id: Int, val date: String, val gameID: Int)
data class addSession(val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)
data class Session(val id: Int, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)