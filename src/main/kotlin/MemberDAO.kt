import java.sql.ResultSet

/**
 * Created by kalk on 6/20/17.
 */
class MemberDAO {

    fun add(firstName: String, lastName: String): Int {
         var res: Int
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("insert into member (first_name, last_name) values (?,?) returning member_id")
            stmt.setString(1,firstName)
            stmt.setString(2,lastName)
            stmt.executeQuery()
            stmt.resultSet.next()
            res = stmt.resultSet.getInt(1)
            con.close()
        } catch (e: Exception) {
            println(e.message)
            throw APIException("could not add member $firstName $lastName")
        }
        return res
    }

    fun update(firstName: String, lastName: String, id: Int): Boolean {
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("update Member set first_name = ?, last_name = ? where member_id = ?")
            stmt.setString(1, firstName)
            stmt.setString(2, lastName)
            stmt.setInt(3, id)
            stmt.execute()
            con.close()
            return true
        } catch (e: Exception) {
            println(e.message)
            throw APIException("could not update member $firstName $lastName")
        }
        return false
    }

    fun get(limit: Int = 100, offset: Int = 0): ArrayList<getMember> {
        val members = ArrayList<getMember>()
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("select * from member limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            val rs: ResultSet
            rs = stmt.executeQuery()
            while (rs.next()) {
                members.add(getMember(id = rs.getInt(1), firstName = rs.getString(2), lastName = rs.getString(3))
            }
            con.close()
        } catch (e: Exception) {
            println(e.message)
        }
        return members
    }

    fun     getDetailed(id: Int): Member {
        val member: Member
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("""select m.first, m.last, w.wins, l.losses, t.timesTraitor from
                                                (select count(member) as wins from winner where member = ?) as w,
                                                (select count(member) as losses from loser where member = ?) as l,
                                                (select count(member) as timesTraitor from traitor where member = ?) as t,
                                                (select first_name as first, last_name as last from member where member_id = ?) as m""")
            val rs: ResultSet
            for(i in 1..4)
                stmt.setInt(i, id)
            rs = stmt.executeQuery()
            rs.next()
            print(rs)
            val wins = rs.getInt(3)
            val losses = rs.getInt(4)
            val total = wins + losses
            member = Member(id = id, firstName = rs.getString(1), lastName = rs.getString(2),
                            wins = wins, winRatio = wins.toDouble()/total, timesTraitor = rs.getInt(5), gamesPlayed = total) //add losses?
            con.close()
        } catch (e: Exception) {
            throw APIException("${e.message}")
        }
        return  member
    }

    fun delete(id: Int): Boolean {
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("delete from member where member_id = ?")
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

data class lightMember(val firstName: String, val lastName: String) {
    init {
            val numbers = Regex(".*\\d+.*")
            require(!firstName.matches(numbers) && !lastName.matches(numbers)) {"Invalid name."}
            require(firstName.length > 1) {"$firstName is invalid, must be at least 2 characters."}
            require(lastName.length > 1) {"$lastName is invalid, Name must be at least 2 characters."}
    }
}
data class getMember(val id: Int, val firstName: String, val lastName: String)
data class Member(val id: Int, val firstName: String, val lastName: String, val wins: Int, val winRatio: Double, val timesTraitor: Int, val gamesPlayed: Int)

