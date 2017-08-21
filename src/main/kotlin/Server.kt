import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging
import spark.Response
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
const val GAME_ID = "/api/games/:id"
const val SESSIONS = "/api/sessions"
const val SESSION_ID ="/api/sessions/:id"
const val JSON ="application/json"
const val HTTP_OK = 200
const val HTTP_CREATED = 201
const val HTTP_NO_CONTENT = 204
const val HTTP_BAD_REQUEST = 400
const val DEFAULT_LIMIT = 100
const val DEFAULT_OFFSET = 0

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
            buildResponse(body=mapper.writeValueAsString(publicEndpoints), response = res)
            res.body()
        }

        post(MEMBERS) { req, res ->
            try {
                val member = mapper.readValue<addMember>(req.body())
                val id = MemberDAO().add(member)
                buildResponse(statusCode=HTTP_CREATED, body = toJSON("id", id), response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(MEMBERS) { req, res ->
            try {
                val params = parseParams(req.queryParams("pageSize"),req.queryParams("pageStart"))
                val result = mapper.writeValueAsString(MemberDAO().get(limit = params.first, offset = params.second))
                buildResponse(body = result, response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(MEMBER_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                buildResponse(body = mapper.writeValueAsString(MemberDAO().getDetailed(id)), response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        put(MEMBER_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                val member = mapper.readValue<addMember>(req.body())
                MemberDAO().update(id, member)
                buildResponse(statusCode=HTTP_NO_CONTENT,body="",response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        delete(MEMBER_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                MemberDAO().delete(id)
                buildResponse(statusCode = HTTP_NO_CONTENT, type="", response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        post(GAMES) { req, res ->
                try {
                    val game = mapper.readValue<AddGame>(req.body())
                    var id = GameDAO().add(game)
                    buildResponse(statusCode=HTTP_CREATED, body = toJSON("id", id), response = res)
                    res.body()
                } catch (e: Exception) {
                    throw APIException("Error: ${e.message}")
                }
        }

        get(GAMES) { req, res ->
            try {
                val params = parseParams(req.queryParams("pageSize"),req.queryParams("pageStart"))
                val result = mapper.writeValueAsString(GameDAO().get(limit = params.first, offset = params.second))
                buildResponse(body = result, response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        put(GAME_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                val game = mapper.readValue<AddGame>(req.body())
                GameDAO().update(id, game)
                buildResponse(statusCode = HTTP_NO_CONTENT, type = "", response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        delete(GAME_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                GameDAO().delete(id)
                buildResponse(statusCode = HTTP_NO_CONTENT, type = "", response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        post(SESSIONS) { req, res ->
            try {
                val session = mapper.readValue<addSession>(req.body())
                val id = SessionDAO().add(gameID = session.gameID, date = dtf.format(LocalDateTime.now()), winners = session.winners,
                        losers = session.losers, traitors = session.traitors)
                buildResponse(statusCode = HTTP_CREATED, body = toJSON("id", id), response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(SESSIONS) { req, res ->
            try {
                val params = parseParams(req.queryParams("pageSize"),req.queryParams("pageStart"))
                val result = mapper.writeValueAsString(SessionDAO().get(limit = params.first, offset = params.second))
                buildResponse(body = result, response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        get(SESSION_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                buildResponse(body=mapper.writeValueAsString(SessionDAO().getDetailed(id)), response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        delete(SESSION_ID) { req, res ->
            try {
                val id = paramToInt(req.params(":id"))
                SessionDAO().delete(id)
                buildResponse(statusCode=HTTP_NO_CONTENT, type ="", response = res)
                res.body()
            } catch (e: Exception) {
                throw APIException("Error: ${e.message}")
            }
        }

        post("/api/admin/generate") { req, res ->
            val user = mapper.readValue<apiUser>(req.body())
            val secret = auth.addUser(user.name, user.email)
            buildResponse(statusCode = HTTP_CREATED, body=toJSON("secret", secret),response = res)
            res.body()
        }

        exception(APIException::class.java, { exception, req, res ->
            val message = exception.message ?: "error with request"
            buildResponse(statusCode= HTTP_BAD_REQUEST, body = toJSON("error_message", message), response = res)
            res.body()
        })
    }

    private fun buildResponse(statusCode:Int = HTTP_OK, type:String = JSON, body: String = "", response: Response): Response {
        response.status(statusCode)
        response.type(type)
        response.body(body)
        return response
    }
    private fun paramToInt(param:String):Int {
        return try {param.toInt()} catch (e:Exception){ throw APIException("invalid id")}
    }

    private fun toJSON(key: String, value: Any): String {
        return "{\"${key}\": \"${value}\"}"
    }

    private fun logRequest(ip:String, method: String, user: String) {
        logger.info("""$method request by $user from $ip""")
    }

    private fun parseParams(p1:String?, p2:String?) : Pair<Int,Int> {
        return Pair(p1?.toInt() ?: DEFAULT_LIMIT, p2?.toInt() ?:DEFAULT_OFFSET)
    }
}