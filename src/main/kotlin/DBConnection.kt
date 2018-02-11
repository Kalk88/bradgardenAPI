import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Singelton, gives access to the database.
 * Created by kalk on 6/22/17.
 */

class DBConnection (url: String,  private val user: String,  private val password: String): Database {
    private val dburl = "jdbc:postgresql://$url"
    /**
     * @Return a connection to the database.
     */
    override fun open(): Connection {
        try {
            val connection = DriverManager.getConnection(dburl, user, password)
            return connection
        } catch (e: SQLException) {
            throw APIException("database connection error ${e.message} $dburl")
        }
    }
}
