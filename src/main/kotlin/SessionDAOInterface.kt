interface SessionDAOInterface {
    fun add(session: addSession): Int
    fun update(id: Int, session: addSession): Boolean
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Session>
    fun getDetailed(id: Int): Session
}