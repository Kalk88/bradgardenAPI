import kotlin.collections.ArrayList

interface DAOInterface<T> {
    fun add(data: T): Int
    fun update(id: Int, data: T): Boolean
    fun delete(id: Int): Boolean
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<T>
    fun getDetailed(id: Int): T
    fun getAll(): ArrayList<T>
}