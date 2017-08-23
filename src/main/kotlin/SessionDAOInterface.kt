interface SessionDAOInterface {
    fun add(session: AddSession): Int
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<LightSession>
    fun getDetailed(id: Int): Session
}