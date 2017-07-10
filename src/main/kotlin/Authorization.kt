import mu.KLogging
import org.apache.commons.dbutils.DbUtils
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

/**
 * Created by kalk on 7/2/17.
 */
class Authorization {
    companion object : KLogging()
     private val HMAC_SHA1 = "HmacSHA1"
  private  val kg = KeyGenerator.getInstance(HMAC_SHA1)
  private  val mac = Mac.getInstance(HMAC_SHA1)
  private  val users = HashMap<String, String>()
    init {
        mac.init(kg.generateKey())
        val connection = DBConnection.instance.open()
        try {
            val stmt = connection.prepareStatement("select name, secret from api_user")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                users.put(rs.getString("name"), rs.getString("secret"))
            }
        } catch (e: Exception) {
            logger.error { e.printStackTrace() }
        } finally {
            DbUtils.close(connection)
        }
        logger.info("Authorization init successful")
    }

    fun authorize(requestKey: String, message: String, user: String): Boolean {
        val token = users[user] ?: throw APIException("User not found")
        val dbKey = calculateHMAC(message,token)
        return (requestKey == dbKey)
    }

    fun calculateHMAC(data: String, key: String): String {
        val secretKeySpec = SecretKeySpec(key.toByteArray(), HMAC_SHA1)
        val mac = Mac.getInstance(HMAC_SHA1)
        mac.init(secretKeySpec)
        return toHexString(mac.doFinal(data.toByteArray()))
    }

    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

    /**
     * @return secret id for name
     */
    fun addUser(user: String, email: String): String {
        val connection = DBConnection.instance.open()
    try {
            val secret = UUID.randomUUID().toString()
            val stmt = connection.prepareStatement("insert into api_user (name, email, secret) values (?,?,?)")
            stmt.setString(1, user)
            stmt.setString(2, email)
            stmt.setString(3, secret)
            stmt.execute()
            users[user] = secret
            return secret
        } catch (e: Exception) {
            logger.error("${e.message}")
            throw APIException("Key generation error")
        } finally {
            DbUtils.close(connection)
        }
    }
}
data class apiUser(val name: String, val email: String)