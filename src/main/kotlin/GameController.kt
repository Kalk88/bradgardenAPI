import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

class GameController(dao: GameDAOInterface): ControllerInterface {
    private val dao: GameDAOInterface = dao
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 0

    override fun add(data: String): String {
        try {
            val game = mapper.readValue<AddGame>(data)
            val id = dao.add(game)
            return id.toString()
        } catch (e:Exception) {
            throw APIException("Could not add game")
        }
    }

    override fun update(id: String, data: String): String {
        try {
            val game = mapper.readValue<AddGame>(data)
            dao.update(id.toInt(), game)
            return id //TODO what to return?
        } catch (e:Exception) {
            throw APIException("Could not update game")
        }
    }

    override fun getFromParams(params: HashMap<String, String>): String {
        try {
            val limit = parseParam(params["pageSize"], DEFAULT_LIMIT)
            val offset = parseParam(params["pageStart"], DEFAULT_OFFSET)
            return mapper.writeValueAsString(dao.get(limit, offset))
        } catch (e:Exception) {
            throw APIException("Could not get games")
        }
    }

    override fun getFromID(id: String): String {
        try {
            val game = dao.getDetailed(id.toInt())
            return  mapper.writeValueAsString(game)
        } catch (e:Exception) {
            throw APIException("Could not get game")
        }
    }

    override fun removeWithID(id: String) {
        try {
            dao.delete(id.toInt())
        } catch (e:Exception) {
            throw APIException("Could not remove game")
        }
    }

    private fun parseParam(param: String?, default: Int): Int{
        return param?.toInt() ?: default
    }
}