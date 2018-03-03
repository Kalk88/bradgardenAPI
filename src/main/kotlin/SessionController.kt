import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KLogging

class SessionController(private val dao: DAOInterface<Session>): ControllerInterface<Session> {

    companion object: KLogging()
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 1

    override fun add(session: Session): String {
        try {
            val id = dao.add(session)
            logger.info { "Added Session" }
            return id.toString()
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not add session")
        }
    }

    override fun update(id: String, data: Session): String {
        throw APIException("Cannot update a session. ARE U TRYIN TO CHEAT?")
    }

    override fun getFromParams(params: HashMap<String, String>): String {
        try {
            val size = params["pageSize"]?.toInt() ?: -1
            val start = params["pageStart"]?.toInt() ?: -1
            val limit = paramOrDefault(size, DEFAULT_LIMIT)
            val offset = paramOrDefault(start, DEFAULT_OFFSET)
            return mapper.writeValueAsString(dao.get(limit, offset-1))
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not retrieve sessions")
        }
    }

    override fun getFromID(id: String): String {
        try {
            val session = dao.getDetailed(id.toInt())
            return mapper.writeValueAsString(session)
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not retrieve session")
        }
    }

    override fun getAll(): ArrayList<Session> {
        try {
            return dao.getAll()
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not retrieve all sessions")
        }
    }


    override fun removeWithID(id: String) {
        try {
            dao.delete(id.toInt())
            logger.info { "Deleted Session $id" }
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not delete session")
        }
    }
}