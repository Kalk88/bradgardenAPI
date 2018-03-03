import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging

class Repository(val db: Database) {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    companion object: KLogging()
    val memberController = MemberController(MemberDAO(db))
    val gameController = GameController(GameDAO(db))
    val sessionController = SessionController(SessionDAO(db))
    var members = memberController.getAll()
    var games = gameController.getAll()
    var sessions = sessionController.getAll()


    fun add(member:Member): String {
        val id = memberController.add(member)

        members.add(Member(id.toInt(),member.firstName, member.lastName, member.wins,
                member.winRatio, member.losses, member.timesTraitor, member.gamesPlayed))
        return id
    }

    fun update(id: String, member: Member): String {
        memberController.update(id, member)
        val index = members.indexOfFirst{it.id == id.toInt()}
        members[index] = member
        return id
    }

    fun getMemberFromParams(params: HashMap<String, String>): String {
        if(params.isEmpty())
            return mapper.writeValueAsString(members)
        return ""
    }

    fun getMemberByID(id: String): String {
        return mapper.writeValueAsString(members.find{ it.id == id.toInt()})
    }

    fun removeMemberWithID(id: String) {
        memberController.removeWithID(id)
        members.removeIf { it.id == id.toInt() }
    }

    fun add(game: Game): String {
        val id = gameController.add(game)
        games.add(Game(id.toInt(), game.name, game.maxNumOfPlayers, game.traitor, game.coop))
        return id
    }

    fun update(id: String, game: Game): String {
        gameController.update(id, game)
        val index = games.indexOfFirst{it.id == id.toInt()}
        games[index] = game
        return id
    }

    fun getGameFromParams(params: HashMap<String, String>): String {
        if(params.isEmpty())
            return mapper.writeValueAsString(games)
        return ""
    }

    fun getGameByID(id: String): String {
        return mapper.writeValueAsString(games.find{it.id == id.toInt()})
    }

    fun removeGameByID(id: String) {
        gameController.removeWithID(id)
        games.removeIf{it.id == id.toInt()}
    }

    fun add(session: Session): String {
        val id = sessionController.add(session)
        sessions.add(Session(id.toInt(), session.date, session.gameID, session.winners, session.losers, session.traitors))

        session.winners.forEach { winner ->
            members.map {
                if(winner == it.id) {
                    it.wins++
                    it.gamesPlayed++
                    it.winRatio = it.wins/(it.gamesPlayed *1.0)
                }
            }
        }

        session.losers.forEach { loser ->
            members.map {
                if(loser == it.id) {
                    it.losses++
                    it.gamesPlayed++
                    it.winRatio = it.wins/(it.gamesPlayed *1.0)
                }
            }
        }
        //games played for traitors will be update in winner or loser update.
        session.traitors.forEach { traitor ->
            members.map {
                if(traitor == it.id) {
                    it.timesTraitor++
                }
            }
        }

        return id
    }

    fun getSessionFromParams(params: HashMap<String, String>): String {
        if(params.isEmpty())
            return mapper.writeValueAsString(sessions)
        return ""
    }

    fun getSessionByID(id: String): String {
        return mapper.writeValueAsString(sessions.find{it.id == id.toInt()})
    }

    fun removeSessionWithID(id: String) {
        sessionController.removeWithID(id)
        val session = sessions.find { it.id == id.toInt() }
        if(session != null) {
            session.winners.forEach { winner ->
                members.map {
                    if(winner == it.id) {
                        it.wins--
                        it.gamesPlayed--
                        it.winRatio = it.wins/(it.gamesPlayed *1.0)
                    }
                }
            }

            session.losers.forEach { loser ->
                members.map {
                    if(loser == it.id) {
                        it.losses--
                        it.gamesPlayed--
                        it.winRatio = it.wins/(it.gamesPlayed *1.0)
                    }
                }
            }
            //games played for traitors will be update in winner or loser update.
            session.traitors.forEach { traitor ->
                members.map {
                    if(traitor == it.id) {
                        it.timesTraitor--
                    }
                }
            }
        }
        sessions.removeIf { it.id == id.toInt() }
    }


}