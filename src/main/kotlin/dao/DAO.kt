package dao
import java.util.*
/**
 * Created by kalk on 8/4/17.
 * Every DAO inherits from this class and overrides the write read vals
 * with its own corresponding write & read behaviour.
 */
class DAO {
    val write = DAOWrite()
    val read = DAORead()

    fun add(obj: Any) : Int {
      return write.add(obj)
    }

    fun update(id: Int) : Boolean {
        return write.update(1)
    }

    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Any> {
        return read.get()
    }

    fun getDetailed(id: Int): Any {
        return read.getDetailed(id)
    }

    fun delete(id: Int): Boolean {
      return write.delete(1)
    }

}