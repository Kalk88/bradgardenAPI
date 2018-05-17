import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging
import spark.ModelAndView
import spark.Response
import spark.Spark.*
import spark.template.mustache.MustacheTemplateEngine
import java.util.*

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
const val ETAG = "etag"
const val IF_MODIFIED_SINCE = "if-modified-since"
const val HTTP_OK = 200
const val HTTP_CREATED = 201
const val HTTP_NO_CONTENT = 204
const val HTTP_BAD_REQUEST = 400

class APIException(message: String) : Exception(message)

class Server {
    private val eTagMap = mutableMapOf(
            MEMBERS to generateEtag(),
            GAMES to generateEtag(),
            SESSIONS to generateEtag()
    )
    companion object: KLogging()

    fun start() {
        staticFiles.location("/public")
        val publicEndpoints = hashMapOf("members" to MEMBERS, "games" to GAMES, "sessions" to SESSIONS)
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val db = HerokuDb()
        //  val auth = Authorization(db)
        val repository = Repository(db.memberDao(), db.gameDao(), db.sessionDao())
        port(
            if(System.getenv("PORT").isNullOrEmpty()) 8080 else System.getenv("PORT").toInt()
        )

        before("/*") {req, res ->
            res.header("Access-Control-Allow-Origin", "*")
        }

        before ("/api/*") {req, res ->
            /* Till we figure this out
            if(req.requestMethod() != "GET")
            {
                try {
                    val params = req.headers("Authorization").split(":")
                    val user = params[0]
                    val key = params[1]
                    logRequest(req.ip(), req.requestMethod(), user)
                    if(!auth.authorize(key, req.body(), user)) {
                        logger.error {"$user, $key and ${req.body()} invalid" }
                        throw APIException("Unauthorized request")
                    }
                } catch (e: Exception) {
                    logger.error { e.printStackTrace() }
                    throw APIException("Unauthorized request") }
            }
            */
        }

        get("/",  { req, res ->
            val members = db.memberDao().getAll()
                    .filter { it.gamesPlayed >= 5 }
                    .sortedWith(compareBy(Member::winRatio))
                    .reversed() //Because im to lazy to write my own comparator
            val res = hashMapOf("members" to members)
            ModelAndView(res, "/leaderboard.html")
        }, MustacheTemplateEngine())

        get("/privacy-policy", {req, res ->
            res.redirect("privacy_policy.html")
        })


        get(ENDPOINTS) { req, res ->
            buildResponse(body=mapper.writeValueAsString(publicEndpoints), response = res)
            res.body()
        }

        post(MEMBERS) { req, res ->
            try {
                val member = mapper.readValue<Member>(req.body())
                val id = repository.add(member)
                eTagMap[MEMBERS] = generateEtag()
                buildResponse(statusCode = HTTP_CREATED, body = toJSON("id", id), response = res)
                res.body()
            }  catch (e: JsonMappingException) {
                throw APIException("invalid JSON")
            }
        }

        get(MEMBERS) { req, res ->
            val params = exctractQueryParams(req.queryMap().toMap())
            if(params.isEmpty() && !req.headers(IF_MODIFIED_SINCE).isNullOrEmpty()
                    && validateEtag(req.headers(IF_MODIFIED_SINCE), MEMBERS)) {
                buildResponse(eTag = eTagMap[MEMBERS]!!, statusCode = HTTP_NO_CONTENT, response = res)
            } else {
                val members = repository.getMemberFromParams(params)
                buildResponse(eTag = eTagMap[MEMBERS]!!, statusCode = HTTP_OK, body = members, response = res)
            }
            res.body()
        }

        get(MEMBER_ID) { req, res ->
            val member = repository.getMemberByID(req.params(":id"))
            buildResponse(body = member, response = res)
            res.body()
        }

        delete(MEMBER_ID) { req, res ->
            repository.removeMemberWithID(req.params(":id"))
            buildResponse(statusCode = HTTP_NO_CONTENT, type="", response = res)
            res.body()
        }

        post(GAMES) { req, res ->
            try {
                val game = mapper.readValue<Game>(req.body())
                val id = repository.add(game)
                eTagMap[GAMES] = generateEtag()
                buildResponse(statusCode=HTTP_CREATED, body = toJSON("id", id), response = res)
                res.body()
            } catch (e: JsonMappingException) {
                throw APIException("invalid JSON")
            }
        }

        get(GAMES) { req, res ->
            val params = exctractQueryParams(req.queryMap().toMap())
            if(params.isEmpty() && !req.headers(IF_MODIFIED_SINCE).isNullOrEmpty()
                    && validateEtag(req.headers(IF_MODIFIED_SINCE), GAMES)) {
                buildResponse(eTag = eTagMap[GAMES]!!, statusCode = HTTP_NO_CONTENT, response = res)
            } else {
                val games = repository.getGameFromParams(params)
                buildResponse(eTag = eTagMap[GAMES]!!, statusCode = HTTP_OK, body = games, response = res)
            }
            res.body()
        }

        put(GAME_ID) { req, res ->
            val game = mapper.readValue<Game>(req.body())
            repository.update(req.params(":id"), game)
            buildResponse(statusCode = HTTP_NO_CONTENT, type = "", response = res)
            res.body()
        }

        delete(GAME_ID) { req, res ->
            repository.removeGameByID(req.params(":id"))
            buildResponse(statusCode = HTTP_NO_CONTENT, type = "", response = res)
            res.body()
        }

        post(SESSIONS) { req, res ->
            try {
                val session = mapper.readValue<Session>(req.body())
                val id = repository.add(session)
                eTagMap[MEMBERS] = generateEtag()
                eTagMap[SESSIONS] = generateEtag()
                buildResponse(statusCode = HTTP_CREATED, body = toJSON("id", id), response = res)
                res.body()
            } catch (e: JsonMappingException) {
                throw APIException("invalid JSON")
            }
        }

        get(SESSIONS) { req, res ->
            val params = exctractQueryParams(req.queryMap().toMap())

            if(params.isEmpty() && !req.headers(IF_MODIFIED_SINCE).isNullOrEmpty()
                    && validateEtag(req.headers(IF_MODIFIED_SINCE), SESSIONS)) {
                buildResponse(eTag = eTagMap[SESSIONS]!!, statusCode = HTTP_NO_CONTENT, response = res)
            }  else {
                val sessions = repository.getSessionFromParams(params)
                buildResponse(eTag = eTagMap[SESSIONS]!!, statusCode = HTTP_OK, body = sessions, response = res)
            }
            res.body()
        }

        get(SESSION_ID) { req, res ->
            val session = repository.getSessionByID(req.params(":id"))
            buildResponse(body=session, response = res)
            res.body()
        }

        delete(SESSION_ID) { req, res ->
            repository.removeSessionWithID(req.params(":id"))
            buildResponse(statusCode=HTTP_NO_CONTENT, type ="", response = res)
            res.body()
        }

        exception(APIException::class.java, { exception, req, res ->
            val message = exception.message ?: "error with request"
            buildResponse(statusCode= HTTP_BAD_REQUEST, body = toJSON("error_message", message), response = res)
            res.body()
        })
    }

    private fun buildResponse(eTag: String = "", statusCode:Int = HTTP_OK, type:String = JSON, body: String = "", response: Response): Response {
        response.header(ETAG, eTag)
        response.status(statusCode)
        response.type(type)
        response.body(body)
        return response
    }

    private fun validateEtag(etag: String , route: String):Boolean {
        return etag == eTagMap[route]
    }

    private fun generateEtag() :String {
        return java.util.UUID.randomUUID().toString()
    }

    private fun toJSON(key: String, value: Any): String {
        return "{\"${key}\": \"${value}\"}"
    }

    private fun exctractQueryParams(queryParams: Map<String, Array<String>>): HashMap<String, String> {
        var params = HashMap<String, String>()
        queryParams.forEach { key, value ->  params[key] = value[0]}
        return params
    }

    private fun logRequest(ip:String, method: String, user: String) {
        logger.info("""$method request by $user from $ip""")
    }

}
