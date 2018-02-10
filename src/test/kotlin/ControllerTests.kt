import com.nhaarman.mockito_kotlin.*
import org.junit.*
import org.junit.Assert.*

class ControllerTests {

    private fun dummyMember(id:Int, first:String = "Anthony", last:String = "Dankfano"): Member { return Member(id, first, last, 100, 1.0, 0, 0, 100) }
    private fun dummyGame (id:Int, name:String = "dummy"): Game { return Game(id, name, 100, true, true) }
    private fun dummySession(id: Int): Session {return Session(id, "date", 1, listOf(1,2,3), listOf(3,2,1), listOf(1))}

    @Test fun creates_a_memberController() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
    }

    @Test fun should_add_member() {
        val mock = mock<MemberDAOInterface> {
            on {add(dummyMember(0))} doReturn 1
        }
        val data = dummyMember(0)
        val controller = MemberController(mock)
        assertEquals(controller.add(data),"1")
    }

    @Test (expected = IllegalArgumentException::class)
    fun should_not_add_member_when_name_is_too_short() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        controller.add(dummyMember(0, first = "fail", last = ""))
    }


    @Test fun should_update_member() {
        val mock = mock<MemberDAOInterface> {
            on {update(1, dummyMember(id=1, last="Updatefano"))} doReturn (true)
        }
        val controller = MemberController(mock)
        assertEquals(controller.update("1", dummyMember(1, first = "Anthony", last = "Updatefano")), "1")

    }

    @Test  (expected = APIException::class)
    fun should_not_update_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<MemberDAOInterface>()
        val controller = MemberController(mock)
        controller.update("wrong", dummyMember(0))
    }


    @Test fun should_return_a_list_of_members() {
        val mocklist = arrayListOf(dummyMember(1), dummyMember(2))
        val mock = mock<MemberDAOInterface> {
            on {get()} doReturn mocklist
        }
        val controller = MemberController(mock)
        val params = hashMapOf<String, String>()
        val membersAsString = controller.getFromParams(params)
        assertEquals("""[{"id":1,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100},{"id":2,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}]""", membersAsString)
    }

    @Test fun should_return_a_list_of_members_when_queryparam_are_zero() {
        val mocklist = arrayListOf(dummyMember(1), dummyMember(2))
        val mock = mock<MemberDAOInterface> {
            on {get(100,0)} doReturn mocklist
        }
        val controller = MemberController(mock)
        val params = hashMapOf<String, String>("pageSize" to "0", "pageStart" to "0")
        val membersAsString = controller.getFromParams(params)
        assertEquals("""[{"id":1,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100},{"id":2,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}]""", membersAsString)
    }

    @Test fun should_return_member_from_ID() {
        val mock = mock<MemberDAOInterface> {
            on {getDetailed(2)} doReturn dummyMember(2)
        }
        val controller = MemberController(mock)
        val membersAsString = controller.getFromID("2")
        assertEquals("""{"id":2,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}""", membersAsString)
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
            on {add(dummyGame(0))} doReturn 1
        }
        val controller = GameController(mock)
        assertEquals(controller.add(dummyGame(0)),"1")
    }


    @Test (expected = IllegalArgumentException::class)
    fun should_not_add_game_when_name_is_too_short() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        controller.add(dummyGame(0, "g"))
    }

    @Test fun should_update_game() {
        val mock = mock<GameDAOInterface> {
            on {update(1, dummyGame(1, "updategame"))} doReturn (true)
        }
        val controller = GameController(mock)
        assertEquals(controller.update("1", dummyGame(1, "updategame")), "1")

    }

    @Test  (expected = APIException::class)
    fun should_not_update_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        controller.update("wrong", dummyGame(1))
    }

    @Test  (expected = IllegalArgumentException::class)
    fun should_fail_when_game_data_is_invalid() {
        val mock = mock<GameDAOInterface>()
        val controller = GameController(mock)
        controller.update("1",dummyGame(1, "g"))
    }

    @Test fun should_return_a_list_of_games() {
        val mock = mock<GameDAOInterface> {
            on {get()} doReturn arrayListOf(dummyGame(1), dummyGame(2))
        }
        val controller = GameController(mock)
        val params = hashMapOf<String, String>()
        val gamesAsString = controller.getFromParams(params)
        assertEquals("""[{"id":1,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true},{"id":2,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true}]""".trimMargin(), gamesAsString)
    }

    @Test fun should_return_a_list_of_games_when_queryparam_are_zero() {
        val mock = mock<GameDAOInterface> {
            on {get(100,0)} doReturn arrayListOf(dummyGame(1), dummyGame(2))
        }
        val controller = GameController(mock)
        val params = hashMapOf<String, String>("pageSize" to "0", "pageStart" to "0")
        val gamesAsString = controller.getFromParams(params)
        assertEquals("""[{"id":1,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true},{"id":2,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true}]""".trimMargin(), gamesAsString)
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

    @Test fun creates_a_SessionController() {
        val mock = mock<SessionDAOInterface>()
        val controller = SessionController(mock)
    }

    @Test fun should_add_a_session() {
        val mock = mock<SessionDAOInterface> {
            on {add(dummySession(0))} doReturn 1
        }
        val controller = SessionController(mock)
        assertEquals(controller.add(dummySession(0)),"1")
    }


    @Test (expected = APIException::class)
    fun should_never_be_able_to_update_a_session() {
        val mock = mock<SessionDAOInterface>()
        val controller = SessionController(mock)
        controller.update("nope", dummySession(0))
    }

    @Test fun should_return_a_list_of_sessions() {
        val mock = mock<SessionDAOInterface> {
            on {get()} doReturn  arrayListOf(dummySession(1), dummySession(2))
        }
        val controller = SessionController(mock)
        val params = hashMapOf<String, String>()
        val sessionsAsString = controller.getFromParams(params)
        assertEquals("""[{"id":1,"date":"date","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]},{"id":2,"date":"date","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]}]""".trimMargin(), sessionsAsString)
    }


    @Test fun should_return_a_list_of_sessions_when_queryparam_are_zero() {
        val mock = mock<SessionDAOInterface> {
            on {get()} doReturn arrayListOf (dummySession(1), dummySession(2))
        }
        val controller = SessionController(mock)
        val params = hashMapOf<String, String>("pageSize" to "0", "pageStart" to "0")
        val sessionsAsString = controller.getFromParams(params)
        assertEquals("""[{"id":1,"date":"date","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]},{"id":2,"date":"date","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]}]""", sessionsAsString)
    }

    @Test fun should_return_session_from_ID() {
        val mock = mock<SessionDAOInterface> {
            on {getDetailed(2)} doReturn dummySession(2)
        }
        val controller = SessionController(mock)
        val gameAsString = controller.getFromID("2")
        assertEquals("""{"id":2,"date":"date","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]}""", gameAsString)
    }

    @Test  (expected = APIException::class)
    fun should_not_return_session_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<SessionDAOInterface>()
        val controller = SessionController(mock)
        controller.getFromID("wrong")
    }

    @Test fun should_remove_session_with_ID() {
        val mock = mock<SessionDAOInterface> {
            on { delete(2)} doReturn true
        }
        val controller = SessionController(mock)
        controller.removeWithID("2")
    }

    @Test  (expected = APIException::class)
    fun should_not_remove_session_when_invalid_ID_cannot_be_converted_to_INT() {
        val mock = mock<SessionDAOInterface>()
        val controller = SessionController(mock)
        controller.removeWithID("wrong")
    }
}