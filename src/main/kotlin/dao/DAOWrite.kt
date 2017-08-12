package dao

/**
 * Created by kalk on 8/12/17.
 * Basic implementation of the DAOWriteInterface
 */
class DAOWrite : DAOWriteInterface {
    override fun add(obj: Any): Int {
        return -1 //value that should never be used in the production
    }

    override fun update(id: Int): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }
}