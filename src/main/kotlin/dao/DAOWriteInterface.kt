package dao

/**
 * Created by kalk on 8/12/17.
 */
interface DAOWriteInterface {

    fun add(obj: Any): Int {
        return 1
    }

    fun update(id: Int) : Boolean {
        return true
    }

    fun delete(id: Int): Boolean {
        return true
    }
}