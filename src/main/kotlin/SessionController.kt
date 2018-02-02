import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class SessionController(daoInterface: SessionDAOInterface): ControllerInterface<Session> {

    companion object: KLogging()
    private val dao: SessionDAOInterface = daoInterface
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 1

    override fun add(data: String): String {
        try {
            val session = mapper.readValue<AddSession>(data)
            val id = dao.add(session)
            logger.info { "Added Session" }
            return id.toString()
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not add session")
        }
    }

    override fun update(id: String, data: String): String {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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