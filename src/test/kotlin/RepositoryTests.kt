import com.nhaarman.mockito_kotlin.*
import org.junit.*
import org.junit.Assert.*

class RepositoryTests {
    private val dateString = "1997-07-16T19:20:30.45+01:00";
    private fun dummyMember(id: Int, first: String = "Anthony", last: String = "Dankfano"): Member {
        return Member(id, first, last, 100, 1.0, 0, 0, 100)
    }

    private fun dummyGame(id: Int, name: String = "dummy"): Game {
        return Game(id, name, 100, true, true)
    }

    private fun dummySession(id: Int): Session {
        return Session(id, dateString, 1, listOf(1, 2, 3), listOf(3, 2, 1), listOf(1))
    }

    private lateinit var mockMember: MemberDAO
    private lateinit var mockGame: GameDAO
    private lateinit var mockSession: SessionDAO

    @Before fun initMocks() {
        mockMember = mock<MemberDAO>()
        mockGame = mock<GameDAO>()
        mockSession = mock<SessionDAO>()
    }

    @Test fun should_add_member() {
        mockMember = mock {
            on {add(dummyMember(0))} doReturn 1
        }
        val data = dummyMember(0)
        val repository = Repository(mockMember, mockGame, mockSession)
        assertEquals(repository.add(data),"1")
    }

    @Test (expected = IllegalArgumentException::class)
    fun should_not_add_member_when_name_is_too_short() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.add(dummyMember(0, first = "fail", last = ""))
    }

    @Test fun should_update_member() {
        mockMember = mock {
            on {getAll()} doReturn arrayListOf(dummyMember(1), dummyMember(2))
            on {update(1, dummyMember(id=1, last="Updatefano"))} doReturn (true)
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        assertEquals(repository.update("1", dummyMember(1, first = "Anthony", last = "Updatefano")), "1")

    }

    @Test  (expected = APIException::class)
    fun should_not_update_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.update("wrong", dummyMember(0))
    }


    @Test fun should_return_a_list_of_members() {
        mockMember = mock {
            on {getAll()} doReturn arrayListOf(dummyMember(1), dummyMember(2))
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf<String, String>()
        val membersAsString = repository.getMemberFromParams(params)
        assertEquals("""[{"id":1,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100},{"id":2,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}]""", membersAsString)
    }

    @Ignore //TODO
    @Test fun should_return_a_list_of_members_when_queryparam_are_zero() {
        mockMember = mock {
            on {getAll()} doReturn arrayListOf(dummyMember(1), dummyMember(2))
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf("pageSize" to "0", "pageStart" to "0")
        val membersAsString = repository.getMemberFromParams(params)
        assertEquals("""[{"id":1,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100},{"id":2,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}]""", membersAsString)
    }

    @Test fun should_return_member_from_ID() {
        mockMember = mock {
            on {getAll()} doReturn arrayListOf(dummyMember(1), dummyMember(2))
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val membersAsString = repository.getMemberByID("2")
        assertEquals("""{"id":2,"firstName":"Anthony","lastName":"Dankfano","wins":100,"winRatio":1.0,"losses":0,"timesTraitor":0,"gamesPlayed":100}""", membersAsString)
    }

    @Test  (expected = APIException::class)
    fun should_not_return_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.getMemberByID("wrong")
    }

    @Test fun should_remove_member_with_ID() {
        mockMember = mock {
            on { delete(2)} doReturn true
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.removeMemberWithID("2")
    }

    @Test  (expected = APIException::class)
    fun should_not_remove_member_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.removeMemberWithID("wrong")
    }

    @Test fun should_add_a_game() {
        mockGame = mock {
            on {add(dummyGame(0))} doReturn 1
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        assertEquals(repository.add(dummyGame(0)),"1")
    }


    @Test (expected = IllegalArgumentException::class)
    fun should_not_add_game_when_name_is_too_short() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.add(dummyGame(0, "g"))
    }

    @Test fun should_update_game() {
        mockGame = mock {
            on {getAll()} doReturn arrayListOf(dummyGame(1), dummyGame(2))
            on {update(1, dummyGame(1, "updategame"))} doReturn (true)
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        assertEquals(repository.update("1", dummyGame(1, "updategame")), "1")

    }

    @Test  (expected = APIException::class)
    fun should_not_update_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.update("wrong", dummyGame(1))
    }

    @Test  (expected = IllegalArgumentException::class)
    fun should_fail_when_game_data_is_invalid() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.update("1",dummyGame(1, "g"))
    }

    @Test fun should_return_a_list_of_games() {
        mockGame = mock {
            on {getAll()} doReturn arrayListOf(dummyGame(1), dummyGame(2))
        }
        val params = hashMapOf<String, String>()
        val repository = Repository(mockMember, mockGame, mockSession)

        val gamesAsString = repository.getGameFromParams(params)
        assertEquals("""[{"id":1,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true},{"id":2,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true}]""".trimMargin(), gamesAsString)
    }

    @Ignore //TODO
    @Test fun should_return_a_list_of_games_when_queryparam_are_zero() {
        mockGame = mock {
            on {getAll()} doReturn arrayListOf(dummyGame(1), dummyGame(2))
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf<String, String>("pageSize" to "0", "pageStart" to "0")
        val gamesAsString = repository.getGameFromParams(params)
        assertEquals("""[{"id":1,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true},{"id":2,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true}]""".trimMargin(), gamesAsString)
    }

    @Test fun should_return_game_from_ID() {
        mockGame = mock {
            on {getAll()} doReturn arrayListOf(dummyGame(1), dummyGame(2))
            on {getDetailed(2)} doReturn dummyGame(2)
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val gameAsString = repository.getGameByID("2")
        assertEquals("""{"id":2,"name":"dummy","maxNumOfPlayers":100,"traitor":true,"coop":true}""", gameAsString)
    }

    @Test  (expected = APIException::class)
    fun should_not_return_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.getGameByID("wrong")
    }

    @Test fun should_remove_game_with_ID() {
        mockGame = mock {
            on {getAll()} doReturn arrayListOf(dummyGame(1), dummyGame(2))
            on { delete(2)} doReturn true
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.removeGameByID("2")
    }

    @Test  (expected = APIException::class)
    fun should_not_remove_game_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.removeGameByID("wrong")
    }


    @Test fun should_add_a_session() {
        mockSession = mock {
            on {add(dummySession(0))} doReturn 1
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        assertEquals(repository.add(dummySession(0)),"1")
    }


    @Test fun should_return_a_list_of_sessions() {
        mockSession = mock {
            on {getAll()} doReturn  arrayListOf(dummySession(1), dummySession(2))
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf<String, String>()
        val sessionsAsString = repository.getSessionFromParams(params)
        assertEquals("""[{"id":1,"date":"$dateString","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]},{"id":2,"date":"$dateString","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]}]""".trimMargin(), sessionsAsString)
    }


    @Ignore //TODO
    @Test fun should_return_a_list_of_sessions_when_queryparam_are_zero() {
        mockSession = mock {
            on {getAll()} doReturn arrayListOf (dummySession(1), dummySession(2))
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf<String, String>("pageSize" to "0", "pageStart" to "0")
        val sessionsAsString = repository.getSessionFromParams(params)
        assertEquals("""[{"id":1,"date":"$dateString","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]},{"id":2,"date":"$dateString","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]}]""", sessionsAsString)
    }

    @Test fun should_return_session_from_ID() {
        mockSession = mock {
            on {getAll()} doReturn arrayListOf (dummySession(1), dummySession(2))
            on {getDetailed(2)} doReturn dummySession(2)
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val gameAsString = repository.getSessionByID("2")
        assertEquals("""{"id":2,"date":"$dateString","gameID":1,"winners":[1,2,3],"losers":[3,2,1],"traitors":[1]}""", gameAsString)
    }

    @Test  (expected = APIException::class)
    fun should_not_return_session_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.getSessionByID("wrong")
    }

    @Test fun should_remove_session_with_ID() {
        mockSession = mock {
            on { delete(2)} doReturn true
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.removeSessionWithID("2")
    }

    @Test  (expected = APIException::class)
    fun should_not_remove_session_when_invalid_ID_cannot_be_converted_to_INT() {
        val repository = Repository(mockMember, mockGame, mockSession)
        repository.removeSessionWithID("wrong")
    }
}