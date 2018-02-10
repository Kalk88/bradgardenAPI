
interface MemberDAOInterface {
    fun add(member: Member): Int
    fun update(id: Int, member: Member): Boolean
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Member>
    fun getDetailed(id: Int): Member
    fun getAll(): ArrayList<Member>
}