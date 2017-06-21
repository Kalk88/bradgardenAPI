/**
 * Created by kalk on 6/20/17.
 */
class MemberDAO(val connection:String) {

    fun add(firstName: String, lastName: String): Int {
        validateName(firstName, lastName)

    }

    fun update(firstName: String, lastName: String, id: Int): Int {
        validateName(firstName, lastName)
    }

    fun get(from: Int = 0, amount: Int = 0): Array<Member> {}

    fun getDetailed(id: Int): Member {}

    fun delete(id: Int): Boolean {}

    private fun validateName(firstName: String, lastName: String) {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) { "Invalid name." }
        require(firstName.length > 1) { "${firstName} is invalid, must be at least 2 characters." }
        require(lastName.length > 1) { "${lastName} is invalid, Name must be at least 2 characters." }
    }
}
data class Member(val id: Int, val firstName: String, val lastName: String, val wins: Int, val winratio: Double, val timestraitor: Int)

