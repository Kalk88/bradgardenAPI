import spark.Spark.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*


/**
 * Created by kalk on 5/29/17.
 */

fun main(args: Array<String>) {

    port(8080)
    val mapper = ObjectMapper().registerModule(KotlinModule())
    val endpoints = Endpoints.publicEndpoints
    get(Endpoints.ENDPOINTS) {req, res -> endpoints}

    post(Endpoints.MEMBERS) {req, res ->
        try {
            val member = mapper.readValue<MemberController.Member>(req.body())
           MemberController().addMember(member.firstName, member.lastName)
        } catch (e: Exception) {
           error(e)
        }

    }
    get(Endpoints.MEMBERS) {req, res -> "[{\"id\": 1,\"firstName\": \"Jens\",\"lastName\": \"Johnny\" }]"}
    put(Endpoints.MEMBERSID) {req, res -> }
    delete(Endpoints.MEMBERSID) {req, res ->}

    post(Endpoints.GAMES) {req, res ->}
    get(Endpoints.GAMES) {req, res -> }
    put(Endpoints.GAMESID) {req, res ->}
    delete(Endpoints.GAMESID) {req, res ->}

    post(Endpoints.SESSIONS) {req, res ->}
    get(Endpoints.SESSIONS) {req, res -> throw APIException("fuck")}
    get(Endpoints.SESSIONSID) {req, res -> }
    delete(Endpoints.SESSIONSID) {req, res ->}

    exception(APIException::class.java, { exception, req, res ->
        res.status(400)
        res.body(exception.message)
    })

}

class APIException(message: String) : Exception(message)