import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging
import spark.Spark.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/**
 * Created by kalk on 7/5/17.
 */
const val ENDPOINTS = "/api/endpoints"
const val MEMBERS = "/api/members"
const val MEMBER_ID = "/api/members/:id"
const val GAMES = "/api/games"
const val GAME_ID = "/api/game/:id"
const val SESSIONS = "/api/sessions"
const val SESSION_ID ="/api/sessions/:id"
const val JSON ="application/json"
const val HTTP_OK = 200
const val HTTP_CREATED = 201
const val HTTP_NO_CONTENT = 204
const val HTTP_BAD_REQUEST = 400

class APIException(message: String) : Exception(message)

class Server {
    companion object: KLogging()
    fun start() {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val publicEndpoints = hashMapOf("members" to MEMBERS, "games" to GAMES, "sessions" to SESSIONS)
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val auth = Authorization()
        port(8080)

        before("/*") {req, res ->
            res.header("Access-Control-Allow-Origin", "*")
        }
        before ("/api/*") {req, res ->
            try {
                val params = req.headers("Authorization").split(":")
                val user = params[0]
                val key = params[1]
                logRequest(req.ip(), req.requestMethod(), user)
                if(!auth.authorize(key, req.body(), user)) {
                    throw APIException("Unauthorized request")
                }
            } catch (e: Exception) {
                logger.error { e.printStackTrace() }
                throw APIException("Unauthorized request") }
        }

        get(ENDPOINTS) { req, res ->
            res.type(JSON)
            mapper.writeValueAsString(publicEndpoints)
        }

        post(MEMBERS) { req, res ->
            try {
                val member = mapper.readValue<addMember>(req.body())
                var id = MemberDAO().add(member.firstName, member.lastName)
                res.type(JSON)
                res.status(HTTP_CREATED)
                toJSON("id", id)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(MEMBERS) { req, res ->
            try {
                val pageStart: Int? = req.queryParams("pageStart")?.toInt()
                val pageSize: Int? = req.queryParams("pageSize")?.toInt()
                res.type(JSON)
                if (pageStart != null && pageSize != null) {
                    mapper.writeValueAsString(MemberDAO().get(limit = pageSize, offset = pageStart))
                } else if (pageStart == null && pageSize != null) {
                    mapper.writeValueAsString(MemberDAO().get(limit = pageSize))
                } else if (pageStart != null && pageSize == null) {
                    mapper.writeValueAsString(MemberDAO().get(offset = pageStart))
                } else {
                    mapper.writeValueAsString(MemberDAO().get())
                }
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(MEMBER_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                res.type(JSON)
                mapper.writeValueAsString(MemberDAO().getDetailed(id))
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        put(MEMBER_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                val member = mapper.readValue<addMember>(req.body())
                if (MemberDAO().update(member.firstName, member.lastName, id = id)) {
                    res.status(HTTP_NO_CONTENT)
                } else {
                    res.status(HTTP_BAD_REQUEST)
                }
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        delete(MEMBER_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                res.status(HTTP_NO_CONTENT)
                MemberDAO().delete(id)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        post(GAMES) { req, res ->
            try {
                val game = mapper.readValue<AddGame>(req.body())
                var id = GameDAO().add(game.name, game.maxNumOfPlayers, game.traitor, game.coop)
                res.type(JSON)
                res.status(HTTP_CREATED)
                toJSON("id", id)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(GAMES) { req, res ->
            try {
                val pageStart: Int? = req.queryParams("pageStart")?.toInt()
                val pageSize: Int? = req.queryParams("pageSize")?.toInt()
                res.type(JSON)
                if (pageStart != null && pageSize != null) {
                    mapper.writeValueAsString(GameDAO().get(limit = pageSize, offset = pageStart))
                } else if (pageStart == null && pageSize != null) {
                    mapper.writeValueAsString(GameDAO().get(limit = pageSize))
                } else if (pageStart != null && pageSize == null) {
                    mapper.writeValueAsString(GameDAO().get(offset = pageStart))
                } else {
                    mapper.writeValueAsString(GameDAO().get())
                }
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        put(GAME_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                val game = mapper.readValue<AddGame>(req.body())
                GameDAO().update(game.name, game.maxNumOfPlayers, game.traitor, game.coop, id)
                res.type(JSON)
                res.status(HTTP_NO_CONTENT)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        delete(GAME_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                res.status(HTTP_NO_CONTENT)
                MemberDAO().delete(id)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        post(SESSIONS) { req, res ->
            try {
                val session = mapper.readValue<addSession>(req.body())
                res.type(JSON)
                res.status(HTTP_CREATED)
                val id = SessionDAO().add(gameID = session.gameID, date = dtf.format(LocalDateTime.now()), winners = session.winners,
                        losers = session.losers, traitors = session.traitors)
                toJSON("id", id)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(SESSIONS) { req, res ->
            try {
                val pageStart: Int? = req.queryParams("pageStart")?.toInt()
                val pageSize: Int? = req.queryParams("pageSize")?.toInt()
                res.type(JSON)
                if (pageStart != null && pageSize != null) {
                    mapper.writeValueAsString(SessionDAO().get(limit = pageSize, offset = pageStart))
                } else if (pageStart == null && pageSize != null) {
                    mapper.writeValueAsString(SessionDAO().get(limit = pageSize))
                } else if (pageStart != null && pageSize == null) {
                    mapper.writeValueAsString(SessionDAO().get(offset = pageStart))
                } else {
                    mapper.writeValueAsString(SessionDAO().get())
                }
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

       get(SESSION_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                res.type(JSON)
                mapper.writeValueAsString(SessionDAO().getDetailed(id))
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

       delete(SESSION_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                res.status(HTTP_NO_CONTENT)
                SessionDAO().delete(id)
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        post("/api/admin/generate") { req, res ->
            val user = mapper.readValue<apiUser>(req.body())
            val secret = auth.addUser(user.name, user.email)
            res.type(JSON)
            toJSON("secret", secret)
        }


       exception(APIException::class.java, { exception, req, res ->
            res.status(HTTP_BAD_REQUEST)
            res.type(JSON)
           val message = exception.message ?: "error with request"
            toJSON("error_message", message)
        })

    }
    fun paramToInt(param:String):Int {
        return try {param.toInt()} catch (e:Exception){ throw APIException("invalid id")}
    }

    fun toJSON(key: String, value: Any): String {
        return "{\"${key}\": \"${value}\"}"
    }

    fun logRequest(ip:String, method: String, user: String) {
        logger.info("""$method request by $user from $ip""")
    }
}