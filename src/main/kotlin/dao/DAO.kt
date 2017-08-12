package dao

import java.util.*

/**
 * Created by kalk on 8/4/17.
 */
class DAO {
    //TODO class that other dao's inherit. specific behaviour is set through interfaces.

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

    fun delete(id: Int): Boolean {
        write.delete(1)
    }

}