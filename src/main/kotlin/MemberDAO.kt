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
            stmt.close()
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
            stmt.close()
            return true
        } catch (e: Exception) {
            println(e.message)
            throw APIException("could not update member $firstName $lastName")
        }
        return false
    }

    fun get(limit: Int = 100, offset: Int = 0): ArrayList<Member> {
        val members = ArrayList<Member>()
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("select * from member limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            val rs: ResultSet
            rs = stmt.executeQuery()
            while (rs.next()) {
                members.add(Member(id = rs.getInt(1), firstName = rs.getString(2), lastName = rs.getString(3),
                                   wins = 0, winRatio = 0.0, timesTraitor =  0, gamesPlayed = 0))
            }
            stmt.close()
        } catch (e: Exception) {
            println(e.message)
        }
        return members
    }

    fun     getDetailed(id: Int): Member {
        val member: Member
        try {
            val con = openConnection()
            // wins from count winner, gamesPlayed from count game_session, winRatio from wins/gamesPlayed, timesTraitor from count traitor...       name the counts?
            val stmt = con.prepareStatement("select * from member where member_id = ?")
            val rs: ResultSet
            stmt.setInt(1, id)
            rs = stmt.executeQuery()
            rs.next()
            member = Member(id = rs.getInt(1), firstName = rs.getString(2), lastName = rs.getString(3),
                            wins = 0, winRatio = 0.0, timesTraitor =  0, gamesPlayed = 0)
            stmt.close()
        } catch (e: Exception) {
            throw APIException("error: ${e.message}")
        }
        return  member
    }

    fun delete(id: Int): Boolean {
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("delete from member where member_id = ?")
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

data class lightMember(val firstName: String, val lastName: String) {
    init {
            val numbers = Regex(".*\\d+.*")
            require(!firstName.matches(numbers) && !lastName.matches(numbers)) {"Invalid name."}
            require(firstName.length > 1) {"$firstName is invalid, must be at least 2 characters."}
            require(lastName.length > 1) {"$lastName is invalid, Name must be at least 2 characters."}
    }
}
data class Member(val id: Int, val firstName: String, val lastName: String, val wins: Int, val winRatio: Double, val timesTraitor: Int, val gamesPlayed: Int)

