import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nhaarman.mockito_kotlin.*
import org.junit.*
import org.junit.Assert.*

class RepositoryTests {


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

    @Test fun should_return_a_list_of_games_when_queryparam_are_zero() {
        val toComp = arrayListOf(dummyGame(1), dummyGame(2))
        mockGame = mock {
            on {getAll()} doReturn toComp
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf("pageSize" to "0", "pageStart" to "0")
        val gamesAsString = repository.getGameFromParams(params)
        assertEquals(jacksonObjectMapper().writeValueAsString(toComp), gamesAsString)
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
        mockMember = mock {
            on {getAll()} doReturn arrayListOf(dummyMember(1, first = "Jens", last = "Johnny"), dummyMember(2, first = "DJ"))
        }
        mockSession = mock {
            on {getAll()} doReturn  arrayListOf(dummySession(1), dummySession(2))
            on {add(dummySession(3))} doReturn 3
        }

        val repository = Repository(mockMember, mockGame, mockSession)

        var jens = repository.getMemberByID("1")
        var dj = repository.getMemberByID("2")
        assert(jens.contains("\"wins\":100"))
        assert(dj.contains("\"wins\":100"))
        assert(jens.contains("\"gamesPlayed\":100"))
        assert(dj.contains("\"gamesPlayed\":100"))
        val res = repository.add(dummySession(3))
        assertEquals(res,"3")
        jens = repository.getMemberByID("1")
        dj = repository.getMemberByID("2")
        assert(jens.contains("\"wins\":101"))
        assert(dj.contains("\"wins\":100"))
        assert(jens.contains("\"gamesPlayed\":101"))
        assert(dj.contains("\"gamesPlayed\":101"))
    }


    @Test fun should_return_a_list_of_sessions() {
        val toComp = arrayListOf (dummySession(1), dummySession(2))
        mockSession = mock {
            on {getAll()} doReturn toComp
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf<String, String>()
        val sessionsAsString = repository.getSessionFromParams(params)
        assertEquals(jacksonObjectMapper().writeValueAsString(toComp), sessionsAsString)
    }

    @Test fun should_return_a_list_of_sessions_when_queryparam_are_zero() {
        val toComp = arrayListOf (dummySession(1), dummySession(2))
        mockSession = mock {
            on {getAll()} doReturn toComp
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val params = hashMapOf("pageSize" to "0", "pageStart" to "0")
        val sessionsAsString = repository.getSessionFromParams(params)
        assertEquals(jacksonObjectMapper().writeValueAsString(toComp), sessionsAsString)
    }

    @Test fun should_return_session_from_ID() {
        val toComp =  dummySession(2)
        mockSession = mock {
            on {getAll()} doReturn arrayListOf (dummySession(1), toComp)
            on {getDetailed(2)} doReturn toComp
        }
        val repository = Repository(mockMember, mockGame, mockSession)
        val gameAsString = repository.getSessionByID("2")

        assertEquals(jacksonObjectMapper().writeValueAsString(toComp), gameAsString)
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