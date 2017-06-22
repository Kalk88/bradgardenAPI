/**
 * Created by kalk on 6/20/17.
 */
class GameDAO(val connection:String)  {

    fun add(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean): Int {
        require(maxNumOfPlayers > 0) {"Number of players must be greater than 0"}
        require(name.length > 1) {"${name} is invalid, must be at least 2 characters."}
        return 0
    }

    fun update(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean, id: Int): Int {
        return 0
    }

    fun get(from:Int = 0, amount: Int = 0): Array<Game>{
        val game = arrayOf(Game(id=0,name="Abrakadabra",maxNumOfPlayers = 111,traitor = true,coop = false))
        return game
    }

    fun remove(id: Int): Boolean{
        return false
    }
}

data class Game(val id: Int, val name: String, val maxNumOfPlayers: Int, val traitor: Boolean, val coop: Boolean)



