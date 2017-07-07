import mu.KLogging

/**
 * Created by kalk on 6/20/17.
 */
class GameDAO  {
    companion object: KLogging()
    fun add(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean): Int {
        var game_id: Int
        try{
            val con = DBConnection.instance.open()
            val stmt = con.prepareStatement("insert into game (name, maxNumOfPlayers, traitor, coop) values (?,?,?,?) returning game_id")
            stmt.setString(1,name)
            stmt.setInt(2,maxNumOfPlayers)
            stmt.setBoolean(3, traitor)
            stmt.setBoolean(4, coop)
            stmt.executeQuery()
            game_id=stmt.resultSet.getInt(1)
            con.close()
        }catch (e: Exception){
            logger.error("Error ADD ${e.message}")
            throw APIException("Could not add game $name")
        }

        return game_id
    }

    fun update(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean, id: Int): Boolean {
        try{
            val con = DBConnection.instance.open()
            val stmt = con.prepareStatement("update game set name = ?, maxNumOfPlayes = ?, traitor = ?, coop = ? where game_id = id")
            stmt.setString(1, name)
            stmt.setInt(2, maxNumOfPlayers)
            stmt.setBoolean(3, traitor)
            stmt.setBoolean(4, coop)
            stmt.executeQuery()
            con.close()
            return true
        }catch (e: Exception){
            logger.error("Error UPDATE ${e.message}")
            throw APIException("could not update game $name")
        }
    }

    fun get(limit:Int = 25, offset: Int = 0): ArrayList<Game>{
        val games = ArrayList<Game>()
        try{
            val con = DBConnection.instance.open()
            val stmt =  con.prepareStatement("select * from game limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            stmt.executeQuery()
            con.close()
        }catch (e:Exception) {
            logger.error("Error GET ${e.message}")
        }

        return games
    }

    fun delete(id: Int): Boolean{
        try{
            val con = DBConnection.instance.open()
            val stmt = con.prepareStatement("delete from game where game_id = ?")
            stmt.setInt(1,id)
            stmt.executeQuery()
            con.close()
            return true
        }catch (e: Exception){
            logger.error("Error DELETE ${e.message}")
            throw APIException("Failed to delete $id")
        }
    }
}

data class Game(val id: Int, val name: String, val maxNumOfPlayers: Int, val traitor: Boolean, val coop: Boolean)
data class AddGame(val name: String, val maxNumOfPlayers: Int,val traitor: Boolean,val coop: Boolean) {
    init {
        require(maxNumOfPlayers > 0) {"Number of players must be greater than 0"}
        require(name.length > 1) {"${name} is invalid, must be at least 2 characters."}
    }
}


