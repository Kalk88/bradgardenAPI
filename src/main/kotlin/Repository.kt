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


    fun add(member:Member): String {
        val id = memberController.add(member)
        members.add(member)
        return id
    }

    fun update(id: String, member: Member): String {
        memberController.update(id, member)
        val index = members.indexOfFirst { it.id == id.toInt() }
        members[index] = member
        return id
    }

    fun getMemberFromParams(params: HashMap<String, String>): String {
        val members = memberController.getFromParams(params)
        return members
    }

    fun getMemberByID(id: String): String {
        val member = members.find { it.id == id.toInt() }
        return mapper.writeValueAsString(member)
    }

    fun removeMemberWithID(id: String) {
        memberController.removeWithID(id)
        members.removeIf { it.id == id.toInt() }
    }

    fun add(game:Game): String {
        val id = gameController.add(game)
        return id
    }

    fun update(id: String, game: Game): String {
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

    fun add(data: Session): String {
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