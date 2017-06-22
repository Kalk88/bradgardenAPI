/**
 * Created by kalk on 6/20/17.
 */
class MemberDAO(val connection:String) {

    fun add(firstName: String, lastName: String): Int {
        validateName(firstName, lastName)
        return 0
    }

    fun update(firstName: String, lastName: String, id: Int): Int {
        validateName(firstName, lastName)
        return 0
    }

    fun get(from: Int = 0, amount: Int = 0): Array<Member> {
        val members = arrayOf(Member(id=1, firstName = "jens", lastName = "olsson", wins = 0, winRatio = 0.0, timesTraitor = 9000))
        return members
    }

    fun getDetailed(id: Int): Member {
        throw APIException("test exception")
        return  Member(id=1, firstName = "jens", lastName = "olsson", wins = 0, winRatio = 0.0, timesTraitor = 9000)
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
data class Member(val id: Int, val firstName: String, val lastName: String, val wins: Int, val winRatio: Double, val timesTraitor: Int)

