
interface MemberDAOInterface {
    fun add(member: AddMember): Int
    fun update(id: Int, member: AddMember): Boolean
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Member>
    fun getDetailed(id: Int): Member
    fun getAll(): ArrayList<Member>
}