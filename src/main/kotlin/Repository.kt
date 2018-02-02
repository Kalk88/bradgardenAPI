import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class Repository(val db: Database) {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    companion object: KLogging()
    val memberDAO = MemberDAO(db)
    val gameDAO = GameDAO(db)
    val sessionDAO = SessionDAO(db)
    var members = memberDAO.getAll()
    var games = gameDAO.getAll()
    var sessions = sessionDAO.getAll()


    fun addMember(data: String): String {
        try {
            val member = mapper.readValue<AddMember>(data)
            val id = memberDAO.add(member)
            logger.info { "Added member ${member.firstName} ${member.lastName}" }
            return id.toString()
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Failed to add member, data was incorrect")
        }
    }

    fun updateMember(id: String, data: String): String {
        try {
            val member = mapper.readValue<AddMember>(data)
            memberDAO.update(id.toInt(), member)
            logger.info { "Updated member ${id}" }
            return id
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not update member")
        }
    }

     fun getMemberFromParams(params: HashMap<String, String>): String {
        TODO()
    }

    fun getMemberByID(id: String): String {
        try {
            val member = memberDAO.getDetailed(id.toInt())
            return mapper.writeValueAsString(member)
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not get member")
        }
    }

   fun removeMemberWithID(id: String) {
        try {
            memberDAO.delete(id.toInt())
            logger.info { "Removed member with $id" }
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could delete member")
        }
    }

    fun addGame(data: String): String {
        try {
            val game = mapper.readValue<AddGame>(data)
            val id = gameDAO.add(game)
            logger.info {"Added game ${game.name}"}
            return id.toString()
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not add game")
        }
    }

    fun updateGame(id: String, data: String): String {
        try {
            val game = mapper.readValue<AddGame>(data)
            gameDAO.update(id.toInt(), game)
            logger.info {"Update game ${game.name}"}
            return id //TODO what to return?
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not update game")
        }
    }

    fun getGameFromParams(params: HashMap<String, String>): String {
        TODO()
    }

  fun getGameByID(id: String): String {
        try {
            val game = gameDAO.getDetailed(id.toInt())
            return  mapper.writeValueAsString(game)
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not get game")
        }
    }

    fun removeGameByID(id: String) {
        try {
            gameDAO.delete(id.toInt())
            logger.info { "Removed game with id $id" }
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not remove game")
        }
    }

    fun addSession(data: String): String {
        try {
            val session = mapper.readValue<AddSession>(data)
            val id = sessionDAO.add(session)
            logger.info { "Added Session" }
            return id.toString()
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not add session")
        }
    }

    fun updateSession(id: String, data: String): String {
        throw APIException("Cannot update a session. ARE U TRYIN TO CHEAT?")
    }

    fun getSessionFromParams(params: HashMap<String, String>): String {
       TODO()
    }

   fun getSessionByID(id: String): String {
        try {
            val session = sessionDAO.getDetailed(id.toInt())
            return mapper.writeValueAsString(session)
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not retrieve session")
        }
    }

    fun removeSessionWithID(id: String) {
        try {
            sessionDAO.delete(id.toInt())
            logger.info { "Deleted Session $id" }
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not delete session")
        }
    }
}