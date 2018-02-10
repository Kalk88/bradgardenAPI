interface SessionDAOInterface {
    fun add(session: Session): Int
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Session>
    fun getDetailed(id: Int): Session
    fun getAll(): ArrayList<Session>
}