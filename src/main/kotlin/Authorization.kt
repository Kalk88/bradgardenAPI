/**
 * Created by kalk on 7/2/17.
 */
class Authorization {

    init {
        try {
            val connection = openConnection()
            // get api users from database and store in hashmap
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun authorize(key: String): Boolean {
        return false
    }

    fun generateKey(email: String): String {
        return ""
    }
}