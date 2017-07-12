import mu.KLogging
import org.apache.commons.dbutils.DbUtils
/**
 * Created by kalk on 6/20/17.
 */
class GameDAO  {
    companion object: KLogging()
    fun add(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean): Int {
        var game_id: Int
        val con = DBConnection.instance.open()
        try{
            val stmt = con.prepareStatement("insert into game (game_name, max_Players, traitor, co_op) values (?,?,?,?) returning game_id")
            stmt.setString(1,name)
            stmt.setInt(2,maxNumOfPlayers)
            stmt.setBoolean(3, traitor)
            stmt.setBoolean(4, coop)
            stmt.executeQuery()
            stmt.resultSet.next()
            game_id=stmt.resultSet.getInt(1)
        } catch (e: Exception){
            logger.error("Error ADD ${e.message}")
            throw APIException("Could not add game $name")
        } finally {
            DbUtils.close(con)
        }
        return game_id
    }

    fun update(name: String, maxNumOfPlayers: Int, traitor: Boolean, coop: Boolean, id: Int): Boolean {
        val con = DBConnection.instance.open()
        try{
            val stmt = con.prepareStatement("update game set game_name = ?, max_Players = ?, traitor = ?, co_op = ? where game_id = $id")
            stmt.setString(1, name)
            stmt.setInt(2, maxNumOfPlayers)
            stmt.setBoolean(3, traitor)
            stmt.setBoolean(4, coop)
            stmt.execute()
            con.close()
            return true
        }catch (e: Exception){
            logger.error("Error UPDATE ${e.message}")
            throw APIException("could not update game $name")
        } finally {
            DbUtils.close(con)
        }
    }

    fun get(limit:Int = 25, offset: Int = 0): ArrayList<Game>{
        val games = ArrayList<Game>()
        val con = DBConnection.instance.open()
        try{
            val stmt =  con.prepareStatement("select * from game limit ? offset ?")
            stmt.setInt(1, limit)
            stmt.setInt(2, offset)
            stmt.executeQuery()
            val rs = stmt.resultSet
            while(rs.next()){
                games.add(Game(id = rs.getInt("game_id"), name = rs.getString("game_name"),maxNumOfPlayers = rs.getInt("max_Players"), traitor = rs.getBoolean("traitor"), coop = rs.getBoolean("co_op")))
            }
            con.close()
        }catch (e:Exception) {
            logger.error("Error GET ${e.message}")
        } finally {
            DbUtils.close(con)
        }
        return games
    }

    fun delete(id: Int): Boolean{
        val con = DBConnection.instance.open()
        try{
            val stmt = con.prepareStatement("delete from game where game_id = ?")
            stmt.setInt(1,id)
            stmt.execute()
            return true
        }catch (e: Exception){
            logger.error("Error DELETE ${e.message}")
            throw APIException("Failed to delete $id")
        } finally {
            DbUtils.close(con)
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


