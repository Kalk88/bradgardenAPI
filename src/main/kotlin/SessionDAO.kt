import org.apache.commons.dbutils.DbUtils
import java.sql.Connection
import java.sql.PreparedStatement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

/**
 * Created by kalk on 6/20/17.
 */
class SessionDAO(private val db: Database): DAOInterface<Session> {

    private val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

    override fun add(session: Session): Int {
        val sessionID: Int
        val con = db.open()
        try {
            val insertSession  = "insert into game_session (game, session_date) values (?, ?) returning session_id"
            val winnerStatement = "insert into winner values (?, ?)"
            val loserStatement = "insert into loser  values (?, ?)"
            val traitorStatement = "insert into traitor values (?, ?)"
            var insert = con.prepareStatement(insertSession)
            insert.setInt(1, session.gameID)
            insert.setString(2, dtf.format(LocalDateTime.now()))
            insert.executeQuery()
            insert.resultSet.next()
            sessionID = insert.resultSet.getInt(1)
            insertRecord(winnerStatement, con, session.winners, sessionID)
            insertRecord(loserStatement, con, session.losers, sessionID)
            if(!session.traitors.isEmpty()) {
                insertRecord(traitorStatement, con, session.traitors, sessionID)
            }
        } catch (e: Exception) {
            throw APIException("Could not add session")
        } finally {
            DbUtils.close(con)
        }
        return sessionID
    }

    override fun update(id: Int, data: Session): Boolean {
        return false //No to be used
    }


    override fun get(limit:Int, offset:Int): ArrayList<Session> {
        val sessions = ArrayList<Session>()
        val con = db.open()
        try {
            val stmt = con.prepareStatement("select * from game_session limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            val rs = stmt.executeQuery()
            while (rs.next()) {
              //  sessions.add(Session(id = rs.getInt(1), gameID = rs.getInt(2), date = rs.getString(3)))
            }
        } catch (e: Exception) {
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
        return sessions
    }

    override fun getDetailed(id: Int): Session {
        val getSession =  "select * from game_session where session_id=?"
        val getWinners =  "select member from winner where game_session = ?"
        val getLosers =   "select member from loser where game_session = ?"
        val getTraitors = "select member from traitor where game_session = ?"
        var session: PreparedStatement
        val con = db.open()
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
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
    }

    override fun getAll(): ArrayList<Session> {
        val sessions = ArrayList<Session>()
        val con = db.open()
        try {
            val stmt = con.prepareStatement("select * from game_session")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                //  sessions.add(Session(id = rs.getInt(1), gameID = rs.getInt(2), date = rs.getString(3)))
            }
        } catch (e: Exception) {
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
        return sessions
    }

    override fun delete(id: Int): Boolean {
        val con = db.open()
        try {
            val stmt = con.prepareStatement("delete from game_session where session_id = ?")
            stmt.setInt(1, id)
            stmt.execute()
            con.close()
            return true
        } catch (e: Exception) {
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

data class Session(val id: Int = -1, val date: String, val gameID: Int, val winners: List<Int>, val losers: List<Int>, val traitors: List<Int>)