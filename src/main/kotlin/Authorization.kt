import mu.KLogging
import org.apache.commons.dbutils.DbUtils
import javax.crypto.KeyGenerator
import javax.crypto.Mac

/**
 * Created by kalk on 7/2/17.
 */
class Authorization {
    companion object: KLogging()
    val kg = KeyGenerator.getInstance("HMAC-MD5")
    init {
        val connection = DBConnection.instance.open()
        try {
            val users = HashMap<String, String>()
            val stmt = connection.prepareStatement("select user, secret from api_user")
            val rs = stmt.executeQuery()
            while(rs.next()) {
                users.put(rs.getString("user"), rs.getString("secret"))
            }
        } catch (e: Exception) {
            logger.error{ e.printStackTrace() }
        } finally {
            DbUtils.close(connection)
        }
        logger.info("Authorization init successful")
    }

    fun authorize(key: String): Boolean {
        return false
    }

//    fun generateKey(user: String, email: String): String {
//        try {
//
//            val connection = open()
//            val secret = kg.generateKey()
//            val mac = Mac.getInstance("HmacMD5");
//            mac.init(secret);
//            val result = mac.doFinal("Hi There".toByteArray());
//            val stmt = connection.prepareStatement("insert into api_user (user, email, secret) values (?,?,?)")
//            stmt.setString(1,user)
//            stmt.setString(2,email)
//            stmt.setString(3 result)
//            stmt.execute()
//            return result
//        } catch (e: Exception) {
//            logger.error { e.printStackTrace() }
//            throw APIException("Key generation error")
//        }
//    }

}