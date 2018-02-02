import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties

/**
 * Singelton, gives access to the database.
 * Created by kalk on 6/22/17.
 */

class DBConnection private constructor(): Database {
    private val dburl : String
    private val user : String
    private val password : String
    init {
        val properties = Properties()
        properties.load(FileInputStream("src/main/resources/server.properties"))
        dburl = "jdbc:postgresql://${properties.getProperty("DBURL")}"
        user = properties.getProperty("DBUSER")
        password = properties.getProperty("DBPASSWORD")
    }
    private object Holder { val INSTANCE = DBConnection()}

    companion object {
        val instance: Database by lazy { Holder.INSTANCE }
    }

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
