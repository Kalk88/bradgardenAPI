import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

class MemberController(daoInterface: MemberDAOInterface) {
    val dao: MemberDAOInterface = daoInterface
    val mapper = ObjectMapper().registerModule(KotlinModule())

    fun add(data: String): Int {
        val member = mapper.readValue<addMember>(data)
        return dao.add(member)
    }

    fun get(params:String = ""): ArrayList<Member> {
        return dao.get()
    }
}