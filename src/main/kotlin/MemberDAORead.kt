import org.apache.commons.dbutils.DbUtils
import java.util.ArrayList

class MemberDAORead: DAOReadInterface {

    override fun get(limit: Int, offset: Int): ArrayList<Any> {
        val members = ArrayList<Any>()
        val con = DBConnection.instance.open()
        try {
            val stmt = con.prepareStatement("select * from member limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                members.add(getMember(id = rs.getInt(1), firstName = rs.getString(2), lastName = rs.getString(3)))
            }
        } catch (e: Exception) {
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
        return members
    }

    override fun getDetailed(id: Int): Any {
        val con = DBConnection.instance.open()
        val member: Member
        try {
            val stmt = con.prepareStatement("""select m.first, m.last, w.wins, l.losses, t.timesTraitor from
                                                (select count(member) as wins from winner where member = ?) as w,
                                                (select count(member) as losses from loser where member = ?) as l,
                                                (select count(member) as timesTraitor from traitor where member = ?) as t,
                                                (select first_name as first, last_name as last from member where member_id = ?) as m""")

            for(i in 1..4)
                stmt.setInt(i, id)
            val rs = stmt.executeQuery()
            rs.next()
            val wins = rs.getInt(3)
            val losses = rs.getInt(4)
            val total = wins + losses
            member = Member(id = id, firstName = rs.getString(1), lastName = rs.getString(2),
                    wins = wins, winRatio = wins.toDouble() / total, losses = losses,
                    timesTraitor = rs.getInt(5), gamesPlayed = total)
        } catch (e: Exception) {
            throw APIException("${e.message}")
        } finally {
            DbUtils.close(con)
        }
        return member
    }

}