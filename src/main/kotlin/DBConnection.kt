import java.io.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Singelton, gives access to the database.
 * Created by kalk on 6/22/17.
 */

class DBConnection private constructor(){
    private val dburl : String
    init {
        dburl = "jdbc:postgresql://db:5432/bradgarden?user=postgres&password=postgres"

        //File("src/main/resources/db.txt").readText()
    }
    private object Holder { val INSTANCE = DBConnection()}

    companion object {
        val instance: DBConnection by lazy { Holder.INSTANCE }
    }

    /**
     * @Return a connection to the database.
     */
    fun open(): Connection {
        try {
            val connection = DriverManager.getConnection(dburl)
            return connection
        } catch (e: SQLException) {
            throw APIException("database connection error" + e.message)
        }
    }
}
