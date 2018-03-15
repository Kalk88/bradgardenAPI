import java.sql.Connection

interface Database {
    fun memberDao(): MemberDAO
    fun gameDao(): GameDAO
    fun sessionDao(): SessionDAO
    fun open(): Connection
}