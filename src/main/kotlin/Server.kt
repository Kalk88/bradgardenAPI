import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging
import spark.Response
import spark.Spark.*

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

class APIException(message: String) : Exception(message)

class Server {
    companion object: KLogging()
    fun start() {

        val publicEndpoints = hashMapOf("members" to MEMBERS, "games" to GAMES, "sessions" to SESSIONS)
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val auth = Authorization()
        val memberController = MemberController(MemberDAO())
        val gameController = GameController(GameDAO())
        val sessionController = SessionController(SessionDAO())
        port(8080)

        before("/*") {req, res ->
            res.header("Access-Control-Allow-Origin", "*")
        }
//        before ("/api/*") {req, res ->
//            try {
//                val params = req.headers("Authorization").split(":")
//                val user = params[0]
//                val key = params[1]+
//                logRequest(req.ip(), req.requestMethod(), user)
//                if(!auth.authorize(key, req.body(), user)) {
//                    throw APIException("Unauthorized request")
//                }
//            } catch (e: Exception) {
//                logger.error { e.printStackTrace() }
//                throw APIException("Unauthorized request") }
//        }

        get(ENDPOINTS) { req, res ->
            buildResponse(body=mapper.writeValueAsString(publicEndpoints), response = res)
            res.body()
        }

        post(MEMBERS) { req, res ->
            val id = memberController.add(req.body())
            buildResponse(statusCode=HTTP_CREATED, body = toJSON("id", id), response = res)
            res.body()
        }

        get(MEMBERS) { req, res ->
            //TODO make work with memberController
        }

        get(MEMBER_ID) { req, res ->
            val member = memberController.getFromID(req.params(":id"))
            buildResponse(body = member, response = res)
            res.body()
        }

        put(MEMBER_ID) { req, res ->
            memberController.update(req.params(":id"), req.body())
            buildResponse(statusCode=HTTP_NO_CONTENT,body="",response = res)
            res.body()
        }

        delete(MEMBER_ID) { req, res ->
            memberController.removeWithID(req.params(":id"))
            buildResponse(statusCode = HTTP_NO_CONTENT, type="", response = res)
            res.body()
        }

        post(GAMES) { req, res ->
            val id = gameController.add(req.body())
            buildResponse(statusCode=HTTP_CREATED, body = toJSON("id", id), response = res)
            res.body()
        }

        get(GAMES) { req, res ->
            //TODO make work with gameController
        }

        put(GAME_ID) { req, res ->
            gameController.update(req.params(":id"), req.body())
            buildResponse(statusCode = HTTP_NO_CONTENT, type = "", response = res)
            res.body()
        }

        delete(GAME_ID) { req, res ->
            gameController.removeWithID(req.params(":id"))
            buildResponse(statusCode = HTTP_NO_CONTENT, type = "", response = res)
            res.body()
        }

        post(SESSIONS) { req, res ->
            val id = sessionController.add(req.body())
            buildResponse(statusCode = HTTP_CREATED, body = toJSON("id", id), response = res)
            res.body()
        }

        get(SESSIONS) { req, res ->
            //TODO make work with sessionsController
        }

        get(SESSION_ID) { req, res ->
            val session = sessionController.getFromID(req.params(":id"))
            buildResponse(body=session, response = res)
            res.body()
        }

        delete(SESSION_ID) { req, res ->
            sessionController.removeWithID(":id")
            buildResponse(statusCode=HTTP_NO_CONTENT, type ="", response = res)
            res.body()
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

    private fun toJSON(key: String, value: Any): String {
        return "{\"${key}\": \"${value}\"}"
    }

    private fun logRequest(ip:String, method: String, user: String) {
        logger.info("""$method request by $user from $ip""")
    }
}