import spark.Spark.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by kalk on 5/29/17.
 */
const val ENDPOINTS = "/api/endpoints"
const val MEMBERS = "/api/members"
const val MEMBERSID = "/api/members/:id"
const val GAMES = "/api/games"
const val GAMESID = "/api/game/:id"
const val SESSIONS = "/api/sessions"
const val SESSIONSID ="/api/sessions/:id"
fun main(args: Array<String>) {
    val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    val publicEndpoints = hashMapOf("members" to MEMBERS, "games" to GAMES, "sessions" to SESSIONS)
    port(8080)
    val mapper = ObjectMapper().registerModule(KotlinModule())
    get(ENDPOINTS) {req, res -> publicEndpoints}

    post(MEMBERS) {req, res ->
        try {
            val member = mapper.readValue<Member>(req.body())
           MemberDAO().addMember(member.firstName, member.lastName)
        } catch (e: Exception) {
           throw APIException("Error")
        }

    }
    get(MEMBERS) {req, res -> "[{\"id\": 1,\"firstName\": \"Jens\",\"lastName\": \"Johnny\" }]"}
    put(MEMBERSID) {req, res -> }
    delete(MEMBERSID) {req, res ->}

    post(GAMES) {req, res ->}
    get(GAMES) {req, res -> }
    put(GAMESID) {req, res ->}
    delete(GAMESID) {req, res ->}

    post(SESSIONS) {req, res -> dtf.format(LocalDateTime.now())}
    get(SESSIONS) {req, res -> throw APIException("fuck")}
    get(SESSIONSID) {req, res -> }
    delete(SESSIONSID) {req, res ->}

    exception(APIException::class.java, { exception, req, res ->
        res.status(400)
        res.body(exception.message)
    })
}

class APIException(message: String) : Exception(message)