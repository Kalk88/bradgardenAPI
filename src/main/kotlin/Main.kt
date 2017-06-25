import spark.Spark.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*
import java.lang.Integer.parseInt
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
const val JSON ="application/json"

fun main(args: Array<String>) {
    val conn = "CONNECTION URL HERE"
    val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    val publicEndpoints = hashMapOf("members" to MEMBERS, "games" to GAMES, "sessions" to SESSIONS)
    val mapper = ObjectMapper().registerModule(KotlinModule())
    port(8080)

    get(ENDPOINTS) {req, res -> publicEndpoints}

    post(MEMBERS) {req, res ->
        try {
            val member = mapper.readValue<lightMember>(req.body())
            res.type(JSON)
          MemberDAO().add(member.firstName, member.lastName)
        } catch (e: Exception) {
           throw APIException("Error")
        }

    }
    get(MEMBERS) {req, res ->
        res.type(JSON)
        mapper.writeValueAsString(MemberDAO().get(amount = 5))
    }

    get(MEMBERSID) {req, res ->
        try {
            val id = parseID(req.params(":id"))
            res.type(JSON)
            mapper.writeValueAsString(MemberDAO().getDetailed(id)) }
        catch (e: Exception) {
            throw APIException("Error: ${e.message}")
        }
    }

    put(MEMBERSID) {req, res ->
        try {
            val id = parseID(req.params(":id"))
            val member = mapper.readValue<lightMember>(req.body())
            res.type(JSON)
            MemberDAO().update(member.firstName, member.lastName, id = id)
        } catch (e: Exception) {
          throw APIException("Error: ${e.message}")
        }
    }
    delete(MEMBERSID) {req, res -> {
        try {
            val id = parseID(req.params(":id"))
            MemberDAO().delete(id)
        } catch (e: Exception) {
        throw APIException("Error: ${e.message}")
    }
    }}

    post(GAMES) {req, res ->}
    get(GAMES) {req, res -> }
    put(GAMESID) {req, res ->}
    delete(GAMESID) {req, res ->}

    post(SESSIONS) {req, res -> dtf.format(LocalDateTime.now())}
    get(SESSIONS) {req, res -> }
    get(SESSIONSID) {req, res -> }
    delete(SESSIONSID) {req, res ->}

    exception(APIException::class.java, { exception, req, res ->
        res.status(400)
        res.body(exception.message)
    })
}

fun parseID(reqID : String): Int {
    val id: Int = try { parseInt(reqID)
    } catch (e: NumberFormatException) {
        throw APIException("${e.message} is not a valid member id")
    }
    return id
}
class APIException(message: String) : Exception(message)