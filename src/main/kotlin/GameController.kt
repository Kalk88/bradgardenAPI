/**
 * Created by kalk on 6/20/17.
 */
class GameController {

    fun addGame(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean) {
        require(maxNumOfPlayers > 0) {"Number of players must be greater than 0"}
        require(name.length > 1) {"${name} is invalid, must be at least 2 characters."}
    }

    fun updateGame(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean, id: Int) {}

    fun getGames(from:Int = 0, amount: Int = 0){}

    fun removeGame(id: Int){}
}


data class Game(val id: Int, val name: String, val maxNumOfPlayers: Int, val traitor: Boolean, val coop: Boolean)



