
import java.sql.SQLException
import java.sql.ResultSet
import java.sql.PreparedStatement
import java.util.LinkedList



/**
 * Created by kalk on 6/20/17.
 */
class MemberDAO {

    fun add(firstName: String, lastName: String): Int {
        validateName(firstName, lastName)
        val con = openConnection()
        return 0
    }

    fun update(firstName: String, lastName: String, id: Int): Int {
        validateName(firstName, lastName)
        return 0
    }

    fun get(from: Int = 0, amount: Int = 0): ArrayList<Member> {
        val members = ArrayList<Member>()
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("select * from member")
            val rs: ResultSet
            rs = stmt.executeQuery()
            while (rs.next()) {
                members.add(Member(id = rs.getInt(1), firstName = rs.getString(2), lastName = rs.getString(3),
                                   wins = 0, winRatio = 0.0, timesTraitor =  0, gamesPlayed = 0))
            }
            closeConnection(con)
        } catch (e: Exception) {
            println(e.message)
        }
        return members
    }

    fun getDetailed(id: Int): Member {
        val member: Member
        try {
            val con = openConnection()
            val stmt = con.prepareStatement("select * from member where member_id = ?")
            val rs: ResultSet
            stmt.setInt(1, id)
            rs = stmt.executeQuery()
            rs.next()
            member = Member(id = rs.getInt(1), firstName = rs.getString(2), lastName = rs.getString(3),
                            wins = 0, winRatio = 0.0, timesTraitor =  0, gamesPlayed = 0)
        } catch (e: Exception) {
            throw APIException("error: ${e.message}")
        }
        return  member
    }

    fun delete(id: Int): Boolean {
        return false
    }

    private fun validateName(firstName: String, lastName: String) {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) { "Invalid name." }
        require(firstName.length > 1) { "${firstName} is invalid, must be at least 2 characters." }
        require(lastName.length > 1) { "${lastName} is invalid, Name must be at least 2 characters." }
    }
}
data class Member(val id: Int, val firstName: String, val lastName: String, val wins: Int, val winRatio: Double, val timesTraitor: Int, val gamesPlayed: Int)

