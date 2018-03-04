import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties

/**
 * Singelton, gives access to the database.
 * Created by kalk on 6/22/17.
 */

class TestDB private constructor(): Database {
    private val dburl : String
    private val user : String
    private val password : String
    init {
        val properties = Properties()
        properties.load(FileInputStream("src/main/resources/server.properties"))
        dburl = "jdbc:postgresql://${properties.getProperty("TESTDBURL")}"
        user = properties.getProperty("TESTDBUSER")
        password = properties.getProperty("TESTDBPASSWORD")
    }
    private object Holder { val INSTANCE = TestDB()}

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
