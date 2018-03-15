import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class HerokuDb: Database {

    override fun memberDao(): MemberDAO { return MemberDAO(this) }
    override fun gameDao(): GameDAO { return GameDAO(this) }
    override fun sessionDao(): SessionDAO { return SessionDAO(this) }

    /**
     * @Return a connection to the database.
     */
    override fun open(): Connection {
        try {
            val connection = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"))
            return connection
        } catch (e: SQLException) {
            throw APIException("database connection error ${e.message}")
        }
    }
}
