import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class MemberController(dao: MemberDAOInterface) : ControllerInterface {
    companion object: KLogging()
    private val dao: MemberDAOInterface = dao
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 0

    override fun add(data: String): String {
        try {
            val member = mapper.readValue<addMember>(data)
            val id = dao.add(member)
            logger.info { "added member ${member.firstName} ${member.lastName}" }
            return id.toString()
        } catch (e: Exception) {
            throw APIException("Failed to add member, data was incorrect")
        }
    }

    override fun update(id: String, data: String): String {
        try {
            val member = mapper.readValue<addMember>(data)
            dao.update(id.toInt(), member)
            return id
        } catch (e: Exception) {
            throw APIException("Could not update member")
        }
    }

    override fun getFromParams(params: HashMap<String, String>): String {
      try {
          val limit = parseParam(params["pageSize"], DEFAULT_LIMIT)
          val offset = parseParam(params["pageStart"], DEFAULT_OFFSET)
          return mapper.writeValueAsString(dao.get(limit, offset))
      } catch (e: Exception) {
          throw APIException("Could not get members")
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
        } catch (e: Exception) {
            throw APIException("Could delete member")
        }
    }

  private fun parseParam(param: String?, default: Int): Int{
      return param?.toInt() ?: default
  }
}