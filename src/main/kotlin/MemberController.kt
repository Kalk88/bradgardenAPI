/**
 * Created by kalk on 6/20/17.
 */
class MemberController {

    fun addMember(firstName: String, lastName: String) {
        validateName(firstName, lastName)
    }

    fun updateMember(firstName: String, lastName: String, id:Int){}

    fun getMembers(from:Int = 0, amount: Int = 0){}

    fun getDetailedMember(id: Int){}

    fun deleteMember(id: Int){}

    private fun validateName(firstName: String, lastName: String) {
        val numbers = Regex(".*\\d+.*")
        require(!firstName.matches(numbers) && !lastName.matches(numbers)) { "Invalid name." }
        require(firstName.length > 1) {"${firstName} is invalid, must be at least 2 characters."}
        require(lastName.length > 1) {"${lastName} is invalid, Name must be at least 2 characters."}
}

data class Member(val id: Int, val firstName: String, val lastName: String, val wins: Int, val winratio: Double, val timestraitor: Int)

