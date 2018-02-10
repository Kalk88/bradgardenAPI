import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class GameController(dao: GameDAOInterface): ControllerInterface<Game> {
    companion object: KLogging()
    private val dao: GameDAOInterface = dao
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 1

    override fun add(game: Game): String {
        try {

            val id = dao.add(game)
            logger.info {"Added game ${game.name}"}
            return id.toString()
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not add game")
        }
    }

    override fun update(id: String, game: Game): String {
        try {

            dao.update(id.toInt(), game)
            logger.info {"Update game ${game.name}"}
            return id //TODO what to return?
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not update game")
        }
    }

    override fun getFromParams(params: HashMap<String, String>): String {
        try {
            val size = params["pageSize"]?.toInt() ?: -1
            val start = params["pageStart"]?.toInt() ?: -1
            val limit = paramOrDefault(size, DEFAULT_LIMIT)
            val offset = paramOrDefault(start, DEFAULT_OFFSET)
            return mapper.writeValueAsString(dao.get(limit, offset-1))
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not get games")
        }
    }

    override fun getFromID(id: String): String {
        try {
            val game = dao.getDetailed(id.toInt())
            return  mapper.writeValueAsString(game)
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not get game")
        }
    }

    override fun getAll(): ArrayList<Game> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun removeWithID(id: String) {
        try {
            dao.delete(id.toInt())
            logger.info { "Removed game with id $id" }
        } catch (e:Exception) {
            logger.error { e.message }
            throw APIException("Could not remove game")
        }
    }
}