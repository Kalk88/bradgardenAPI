import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
/**
 * Created by kalk on 6/22/17.
 */
fun openConnection() : Connection {
    try {
      val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bradgarden?user=postgres&password=postgres")
        return connection
    } catch (e: SQLException) {
        throw APIException("database connection error" + e.message)
    }
}

fun closeConnection(connection: Connection) {
    try {
        connection.close()
    } catch (e: SQLException) {
        e.printStackTrace()
    }

}