import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class Repository(val db: Database) {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    companion object: KLogging()
    val memberController = MemberController(MemberDAO(db))
    val gameController = GameController(GameDAO(db))
    val sessionController = SessionController(SessionDAO(db))
    var members = memberController.getAll()
    var games = gameController.getAll()
    var sessions = sessionController.getAll()


    fun addMember(data: String): String {
        TODO()
    }

    fun updateMember(id: String, data: String): String {
        TODO()
    }

    fun getMemberFromParams(params: HashMap<String, String>): String {
        TODO()
    }

    fun getMemberByID(id: String): String {
        TODO()
    }

    fun removeMemberWithID(id: String) {
        TODO()
    }

    fun addGame(data: String): String {
        TODO()
    }

    fun updateGame(id: String, data: String): String {
        TODO()
    }

    fun getGameFromParams(params: HashMap<String, String>): String {
        TODO()
    }

    fun getGameByID(id: String): String {
        TODO()
    }

    fun removeGameByID(id: String) {
        TODO()
    }

    fun addSession(data: String): String {
        TODO()
    }

    fun getSessionFromParams(params: HashMap<String, String>): String {
        TODO()
    }

    fun getSessionByID(id: String): String {
        TODO()
    }

    fun removeSessionWithID(id: String) {
        TODO()
    }
}