import java.util.*
/**
 * Created by kalk on 8/4/17.
 * Every DAO inherits from this class and overrides the write read vals
 * with its own corresponding write & read behaviour.
 */
abstract class DAO {
    abstract fun add(obj: Any) : Int
    abstract fun update(id: Int, obj: Any) : Boolean
    abstract fun get(limit: Int = 100, offset:Int = 0): ArrayList<Any>
    abstract fun getDetailed(id: Int): Any
    abstract fun delete(id: Int): Boolean
}