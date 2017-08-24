import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class MemberController(dao: MemberDAOInterface) : ControllerInterface {
    companion object: KLogging()
    private val dao: MemberDAOInterface = dao
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 1

    override fun add(data: String): String {
        try {
            val member = mapper.readValue<AddMember>(data)
            val id = dao.add(member)
            logger.info { "added member ${member.firstName} ${member.lastName}" }
            return id.toString()
        } catch (e: Exception) {
            throw APIException("Failed to add member, data was incorrect")
        }
    }

    override fun update(id: String, data: String): String {
        try {
            val member = mapper.readValue<AddMember>(data)
            dao.update(id.toInt(), member)
            return id
        } catch (e: Exception) {
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
          throw APIException("Could not get members ${e.message}")
      }
    }

    override fun getFromID(id: String): String {
        try {
            val member = dao.getDetailed(id.toInt())
            return mapper.writeValueAsString(member)
        } catch (e: Exception) {
            throw APIException("Could not get member")
        }
    }

    override fun removeWithID(id: String) {
        try {
            dao.delete(id.toInt())
            logger.info { "Removed member $id" }
        } catch (e: Exception) {
            throw APIException("Could delete member")
        }
    }
}