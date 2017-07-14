import mu.KLogging
import org.apache.commons.dbutils.DbUtils
import java.sql.Connection
import java.sql.PreparedStatement
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
            val winnerStatement = "insert into winner values (?, ?)"
            val loserStatement = "insert into loser  values (?, ?)"
            val traitorStatement = "insert into traitor values (?, ?)"
            var session = con.prepareStatement(insertSession)
            session.setInt(1, gameID)
            session.setString(2, date)
            session.executeQuery()
            session.resultSet.next()
            sessionID = session.resultSet.getInt(1)
            insertRecord(winnerStatement, con, winners, sessionID)
            insertRecord(loserStatement, con, losers, sessionID)
            if(!traitors.isEmpty()) {
                insertRecord(traitorStatement, con, traitors, sessionID)
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
            val rs = stmt.executeQuery()
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
        var session: PreparedStatement
        val con = DBConnection.instance.open()
        try {
            session = con.prepareStatement(getSession)
            session.setInt(1, id)
            session.executeQuery()
            session.resultSet.next()

            val w = retrieveRecord(getWinners, con, id)
            val l = retrieveRecord(getLosers, con, id)
            val t = retrieveRecord(getTraitors, con, id)

            return Session(id, gameID = session.resultSet.getInt(2), date = session.resultSet.getString(3),
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

    private fun insertRecord(statement:String, con: Connection, members: List<Int>, sessionID: Int) {
        var stmt : PreparedStatement
        for (member in members) {
            stmt = con.prepareStatement(statement)
            stmt.setInt(1, sessionID)
            stmt.setInt(2, member)
            stmt.execute()
        }
    }

    private fun retrieveRecord(statement: String, con: Connection, id: Int): ArrayList<Int> {
        val stmt = con.prepareStatement(statement)
        stmt.setInt(1, id)
        stmt.executeQuery()
        val list = ArrayList<Int>()
        while (stmt.resultSet.next()) {
            list.add(stmt.resultSet.getInt(1))
        }
        return list
    }
}
data class lightSession(val id: Int, val date: String, val gameID: Int)
data class addSession(val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)
data class Session(val id: Int, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)