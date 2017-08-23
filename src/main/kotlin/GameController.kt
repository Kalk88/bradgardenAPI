import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class GameController(dao: GameDAOInterface): ControllerInterface {
    private val dao: GameDAOInterface = dao
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val DEFAULT_LIMIT = 100
    private val DEFAULT_OFFSET = 0

    override fun add(data: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(id: String, data: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFromParams(params: HashMap<String, String>): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFromID(id: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeWithID(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}