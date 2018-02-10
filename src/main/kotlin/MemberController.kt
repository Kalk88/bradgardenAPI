import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class MemberController(private val dao: DAOInterface<Member>) : ControllerInterface<Member> {

    companion object: KLogging()
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 1

    override fun add(member: Member): String {
        try {

            val id = dao.add(member)
            logger.info { "Added member ${member.firstName} ${member.lastName}" }
            return id.toString()
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Failed to add member, data was incorrect")
        }
    }

    override fun update(id: String, member: Member): String {
        try {
            dao.update(id.toInt(), member)
            logger.info { "Updated member ${id}" }
            return id
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not update member")
        }
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
            throw APIException("Could not get members ${e.message}")
        }
    }

    override fun getFromID(id: String): String {
        try {
            val member = dao.getDetailed(id.toInt())
            return mapper.writeValueAsString(member)
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could not get member")
        }
    }

    override fun getAll(): ArrayList<Member> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeWithID(id: String) {
        try {
            dao.delete(id.toInt())
            logger.info { "Removed member with $id" }
        } catch (e: Exception) {
            logger.error { e.message }
            throw APIException("Could delete member")
        }
    }
}