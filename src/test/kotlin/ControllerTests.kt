import com.nhaarman.mockito_kotlin.*
import org.junit.*
import org.junit.Assert.*

class ControllerTests {

    private fun dummy (id:Int): Member { return Member(id, "dummy", "test", 100, 1.0, 0, 0, 100) }
    private fun dummyGame (id:Int): Game { return Game(id, "dummy", 100, true, true) }

    @Test fun creates_a_memberController() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
    }

    @Test fun should_add_member() {
        val mock = mock<MemberDAOInterface> {
            on {add(AddMember("Anthony", "Dankfano"))} doReturn 1
        }
        val data = """{ "firstName": "Anthony", "lastName": "Dankfano" }"""
        val controller = MemberController(mock)
        assertEquals(controller.add(data),"1")
    }


    @Test (expected = APIException::class)
    fun should_not_add_member_when_name_is_too_short() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        val data = """{ "firstName": "fail", "lastName": "" }"""
        controller.add(data)
    }

    @Test (expected = APIException::class)
    fun should_not_add_member_when_field_is_missing() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        val data = """{ "firstName": "fail"}"""
        controller.add(data)
    }

    @Test fun should_update_member() {
        val mock = mock<MemberDAOInterface> {
            on {update(1, AddMember("Anthony", "Dankfano"))} doReturn (true)
        }
        val data = """{ "firstName": "Anthony", "lastName": "Updatefano" }"""
        val controller = MemberController(mock)
        assertEquals(controller.update("1", data), "1")

    }

    @Test  (expected = APIException::class)
    fun should_not_update_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<MemberDAOInterface>()
        val data = """{ "firstName": "Anthony", "lastName": "Updatefano" }"""
        val controller = MemberController(mock)
        controller.update("wrong", data)
    }

    @Test  (expected = APIException::class)
    fun should_fail_when_member_data_is_invalid() {
        val mock = mock<MemberDAOInterface>()
        val data = """{ "firstName": "fail"}"""
        val controller = MemberController(mock)
        controller.update("1", data)
    }

    @Test fun should_return_a_list_of_members() {
        val mocklist = arrayListOf(dummy(1), dummy(2))
        val mock = mock<MemberDAOInterface> {
            on {get()} doReturn mocklist
        }
        val controller = MemberController(mock)
        val params = hashMapOf<String, String>()
        val membersAsString = controller.getFromParams(params)
        //TODO something that checks the return
    }

    @Test fun should_return_member_from_ID() {
        val mock = mock<MemberDAOInterface> {
            on {getDetailed(2)} doReturn dummy(2)
        }
        val controller = MemberController(mock)
        val membersAsString = controller.getFromID("2")
        assertEquals("""{"id":2,"firstName":"dummy","lastName":"test","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}""", membersAsString)
    }

    @Test  (expected = APIException::class)
    fun should_not_return_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        controller.getFromID("wrong")
    }

    @Test fun should_remove_member_with_ID() {
        val mock = mock<MemberDAOInterface> {
            on { delete(2)} doReturn true
        }
        val controller = MemberController(mock)
        controller.removeWithID("2")
    }

    @Test  (expected = APIException::class)
    fun should_not_remove_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        controller.removeWithID("wrong")
    }

    @Test fun creates_a_GameController() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
    }

    @Test fun should_add_a_game() {
        val mock = mock<GameDAOInterface> {
            on {add(AddGame("game",6,false,true))} doReturn 1
        }
        val data = """{ "name": "game","maxNumOfPlayers": "6","traitor": "false","coop": "true" }"""
        val controller = GameController(mock)
        assertEquals(controller.add(data),"1")
    }


    @Test (expected = APIException::class)
    fun should_not_add_game_when_name_is_too_short() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        val data = """{ "name": "g","maxNumOfPlayers": "6","traitor": "false","coop": "true" }"""
        controller.add(data)
    }

    @Test (expected = APIException::class)
    fun should_not_add_game_when_field_is_missing() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        val data =  """{ "maxNumOfPlayers": "6","traitor": "false","coop": "true" }"""
        controller.add(data)
    }

    @Test fun should_update_game() {
        val mock = mock<GameDAOInterface> {
            on {update(1, AddGame("updategame",6,false,true))} doReturn (true)
        }
        val data = """{ "name": "updategame","maxNumOfPlayers": "6","traitor": "false","coop": "true" }"""
        val controller = GameController(mock)
        assertEquals(controller.update("1", data), "1")

    }

    @Test  (expected = APIException::class)
    fun should_not_update_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<MemberDAOInterface>()
        val data = """{ "name": "updategame","maxNumOfPlayers": "6","traitor": "false","coop": "true" }"""
        val controller = MemberController(mock)
        controller.update("wrong", data)
    }

    @Test  (expected = APIException::class)
    fun should_fail_when_game_data_is_invalid() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        val data = """{ "name": "g","maxNumOfPlayers": "6","traitor": "false","coop": "true" }"""
        controller.update("1",data)
    }

    @Test fun should_return_a_list_of_games() {
        val mocklist = arrayListOf(dummyGame(1), dummyGame(2))
        val mock = mock<GameDAOInterface> {
            on {get()} doReturn mocklist
        }
        val controller = GameController(mock)
        val params = hashMapOf<String, String>()
        val gameAsString = controller.getFromParams(params)
        //TODO something that checks the return
    }

    @Test fun should_return_game_from_ID() {
        val mock = mock<GameDAOInterface> {
            on {getDetailed(2)} doReturn dummyGame(2)
        }
        val controller = GameController(mock)
        val gameAsString = controller.getFromID("2")
        assertEquals("""{"id":2,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true}""", gameAsString)
    }

    @Test  (expected = APIException::class)
    fun should_not_return_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        controller.getFromID("wrong")
    }

    @Test fun should_remove_game_with_ID() {
        val mock = mock<GameDAOInterface> {
            on { delete(2)} doReturn true
        }
        val controller = GameController(mock)
        controller.removeWithID("2")
    }

    @Test  (expected = APIException::class)
    fun should_not_remove_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        controller.removeWithID("wrong")
    }
}