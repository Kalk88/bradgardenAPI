import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KLogging

class Repository(private val memberDao: MemberDAO,
                 private val gameDao: GameDAO,
                 private val sessionDao: SessionDAO) {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    companion object: KLogging()

    private var members = memberDao.getAll()
    private var games = gameDao.getAll()
    private var sessions = sessionDao.getAll()


    fun add(member:Member): String {
        val id = memberDao.add(member)
        members.add(Member(id,member.firstName, member.lastName, member.wins,
                member.winRatio, member.losses, member.timesTraitor, member.gamesPlayed))
        return id.toString()
    }

    fun update(id: String, member: Member): String {
        try {
            memberDao.update(id.toInt(), member)
            val index = members.indexOfFirst{it.id == id.toInt()}
            members[index] = member
        } catch (e: NumberFormatException) {
            logger.error { e }
            throw APIException ("Invalid id")
        }
        return id
    }

    fun getMemberFromParams(params: HashMap<String, String>): String {
        if(params.isEmpty())
            return mapper.writeValueAsString(members)
        return ""
    }

    fun getMemberByID(id: String): String {
        try {
            val memberid = id.toInt()
            return mapper.writeValueAsString(members.find{ it.id == memberid })
        } catch (e: NumberFormatException) {
            logger.error { e }
            throw APIException("Invald id")
        }
    }

    fun removeMemberWithID(id: String) {
        try {
            memberDao.delete(id.toInt())
            members.removeIf { it.id == id.toInt() }
        } catch (e: NumberFormatException) {
            logger.error { e }
            throw APIException("Invalid id")
        }
    }

    fun add(game: Game): String {
        val id = gameDao.add(game)
        games.add(Game(id, game.name, game.maxNumOfPlayers, game.traitor, game.coop))
        return id.toString()
    }

    fun update(id: String, game: Game): String {
        try {
            gameDao.update(id.toInt(), game)
            val index = games.indexOfFirst{ it.id == id.toInt() }
            games[index] = game
        } catch (e: NumberFormatException) {
            logger.error { e }
            throw APIException("Invalid id")
        }
        return id
    }

    fun getGameFromParams(params: HashMap<String, String>): String {
        if(params.isEmpty())
            return mapper.writeValueAsString(games)
        return ""
    }

    fun getGameByID(id: String): String {
        try {
            val gameId = id.toInt()
            return mapper.writeValueAsString(games.find{ it.id == gameId })
        } catch (e: NumberFormatException) {
            logger.error { e }
            throw APIException("Invalid id")
        }

    }

    fun removeGameByID(id: String) {
        try {
            gameDao.delete(id.toInt())
            games.removeIf{it.id == id.toInt()}
        } catch (e: NumberFormatException) {
        logger.error { e }
        throw APIException("Invalid id")
    }

    }

    fun add(session: Session): String {
        val id = sessionDao.add(session)
        sessions.add(Session(id, session.date, session.gameID, session.winners, session.losers, session.traitors))

        session.winners.forEach { winner ->
            members.filter{ it.id == winner }
                    .map {
                        it.wins++
                        it.gamesPlayed++
                        it.winRatio = it.wins/(it.gamesPlayed *1.0)
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

        return id.toString()
    }

    fun getSessionFromParams(params: HashMap<String, String>): String {
        if(params.isEmpty())
            return mapper.writeValueAsString(sessions)
        return ""
    }

    fun getSessionByID(id: String): String {
        try {
            val sessionId = id.toInt()
            return mapper.writeValueAsString(sessions.find{ it.id == sessionId })
        } catch (e:NumberFormatException) {
            logger.error { e }
            throw APIException("Invalid id")
        }

    }

    fun removeSessionWithID(id: String) {
        try {
            val sessionId = id.toInt()
            sessionDao.delete(sessionId)
            val session = sessions.find { it.id == sessionId }
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
            sessions.removeIf { it.id == sessionId }
        } catch (e: NumberFormatException) {
            logger.error { e }
            throw APIException("Invalid id")
        }

    }


}