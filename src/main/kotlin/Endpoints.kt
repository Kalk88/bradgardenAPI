/**
* Created by kalk on 5/31/17.
*/
class Endpoints {
   companion object endpoint {
        const val ENDPOINTS = "/api/endpoints"
        const val MEMBERS = "/api/members"
        const val MEMBERSID = "/api/members/:id"
        const val GAMES = "/api/games"
        const val GAMESID = "/api/game/:id"
        const val SESSIONS = "/api/sessions"
        const val SESSIONSID ="/api/sessions/:id"
        val publicEndpoints = hashMapOf("members" to MEMBERS, "games" to GAMES, "sessions" to SESSIONS);
    }
}

