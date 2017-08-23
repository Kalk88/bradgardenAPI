import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class SessionController(daoInterface: SessionDAOInterface): ControllerInterface {
    companion object: KLogging()
    private val dao: SessionDAOInterface = daoInterface
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 0

    override fun add(data: String): String {
        try {
            val session = mapper.readValue<AddSession>(data)
            val id = dao.add(session)
            logger.info { "Added Session" }
            return id.toString()
        } catch (e: Exception) {
            throw APIException("")
        }
    }

    override fun update(id: String, data: String): String {
            throw APIException("Cannot update a session. ARE U TRYIN TO CHEAT?")
    }

    override fun getFromParams(params: HashMap<String, String>): String {
        try {
            val limit = parseParam(params["pageSize"], DEFAULT_LIMIT)
            val offset = parseParam(params["pageStart"], DEFAULT_OFFSET)
            return mapper.writeValueAsString(dao.get(limit, offset))
        } catch (e: Exception) {
            throw APIException("Could not retrieve sessions")
        }
    }

    override fun getFromID(id: String): String {
        try {
            val session = dao.getDetailed(id.toInt())
            return mapper.writeValueAsString(session)
        } catch (e: Exception) {
            throw APIException("Could not retrieve session")
        }
    }

    override fun removeWithID(id: String) {
        try {
            dao.delete(id.toInt())
            logger.info { "Deleted Session $id" }
        } catch (e: Exception) {
            throw APIException("Could not delete session")
        }
    }
}