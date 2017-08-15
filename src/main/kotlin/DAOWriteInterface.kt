/**
 * Created by kalk on 8/12/17.
 */
interface DAOWriteInterface {
   fun add(obj: Any): Int
   fun update(id: Int, obj: Any): Boolean
   fun delete(id: Int): Boolean
}