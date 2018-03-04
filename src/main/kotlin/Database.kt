import java.sql.Connection

interface Database {
    fun open(): Connection
}