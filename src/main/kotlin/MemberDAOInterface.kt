
interface MemberDAOInterface {
    fun add(member: addMember): Int
    fun update(id: Int, member: addMember): Boolean
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Member>
    fun getDetailed(id: Int): Member
}