import java.util.ArrayList

/**
 * Created by kalk on 8/12/17.
 *
 */
interface DAOReadInterface {
    fun get(limit: Int = 100, offset:Int = 0): ArrayList<Any>
    fun getDetailed(id: Int): Any
}