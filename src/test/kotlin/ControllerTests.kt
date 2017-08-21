import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nhaarman.mockito_kotlin.*
import org.junit.*
import org.junit.Assert.*

class ControllerTests {

val dummyMember = Member(1, "dummy", "test", 100, 1.0, 0, 0, 100)

    @Test fun creates_a_memberController() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        assertEquals(controller::class, MemberController::class)
    }

    @Test fun should_add_member() {
        val mock = mock<MemberDAOInterface> {
            on {add(addMember("Anthony", "Dankfano"))} doReturn 1
        }
        val member = """{ "firstName": "Anthony", "lastName": "Dankfano" }"""
        val controller = MemberController(mock)
        assertEquals(controller.add(member), 1)
    }

    @Test (expected = JsonMappingException::class)
    fun should_not_add_member_when_name_is_too_short() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        val member = """{ "firstName": "fail", "lastName": "" }"""
        controller.add(member)
    }

    @Test (expected = JsonMappingException::class)
    fun should_not_add_member_when_field_is_missing() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        val member = """{ "firstName": "fail"}"""
        controller.add(member)
    }

    @Test fun should_return_a_list_of_members() {
        val mock = mock<MemberDAOInterface> {
            on {get()} doReturn arrayListOf(dummyMember, dummyMember)
        }
        val controller = MemberController(mock)
        assertEquals(controller.get().size , 2)
    }
}