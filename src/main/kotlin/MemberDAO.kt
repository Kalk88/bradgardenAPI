import mu.KLogging
import java.util.ArrayList

/**
 * Created by kalk on 6/20/17.
 */
class MemberDAO: DAO() {
    companion object: KLogging()

    val write = MemberDAOWrite()
    val read = MemberDAORead()

    override fun add(obj: Any): Int {
        return write.add(obj)
    }

    override fun update(id: Int, obj: Any): Boolean {
        return write.update(id, obj)
    }

    override fun get(limit: Int, offset: Int): ArrayList<Any> {
        return read.get(limit, offset)
    }

    override fun getDetailed(id: Int): Any {
        return read.getDetailed(id)
    }

    override fun delete(id: Int): Boolean {
       return write.delete(id)
    }
}

data class addMember(val firstName: String, val lastName: String) {
    init {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) {"Invalid name."}
        require(firstName.length > 1) {"$firstName is invalid, must be at least 2 characters."}
        require(lastName.length > 1) {"$lastName is invalid, Name must be at least 2 characters."}
    }
}

data class getMember(val id: Int, val firstName: String, val lastName: String)

data class Member(val id: Int, val firstName: String, val lastName: String,
                  val wins: Int, val winRatio: Double, val losses: Int,
                  val timesTraitor: Int, val gamesPlayed: Int)

