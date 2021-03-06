import mu.KLogging
import org.apache.commons.dbutils.DbUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by kalk on 6/20/17.
 */
class GameDAO(private val db: Database): DAOInterface<Game>  {

    companion object: KLogging()

    override fun add(game: Game): Int {
        var gameId: Int
        val con = db.open()
        try{
            val stmt = con.prepareStatement("insert into game (game_name, max_Players, traitor, co_op) values (?,?,?,?) returning game_id")
            stmt.setString(1,game.name)
            stmt.setInt(2,game.maxNumOfPlayers)
            stmt.setBoolean(3, game.traitor)
            stmt.setBoolean(4, game.coop)
            stmt.executeQuery()
            stmt.resultSet.next()
            gameId=stmt.resultSet.getInt(1)
        } catch (e: Exception){
            logger.error("Error ADD ${e.message}")
            throw APIException("Could not add game $game.name")
        } finally {
            DbUtils.close(con)
        }
        return gameId
    }

    override fun update(id: Int, game: Game): Boolean {
        val con = db.open()
        try{
            val stmt = con.prepareStatement("update game set game_name = ?, max_Players = ?, traitor = ?, co_op = ? where game_id = $id")
            stmt.setString(1, game.name)
            stmt.setInt(2, game.maxNumOfPlayers)
            stmt.setBoolean(3, game.traitor)
            stmt.setBoolean(4, game.coop)
            stmt.execute()
            con.close()
            return true
        }catch (e: Exception){
            logger.error("Error UPDATE ${e.message}")
            throw APIException("could not update game $game.name")
        } finally {
            DbUtils.close(con)
        }
    }

    override fun get(limit: Int, offset:Int): ArrayList<Game> {
        val games = ArrayList<Game>()
        val con = db.open()
        try{
            val stmt =  con.prepareStatement("select * from game limit ? offset ? where active = true")
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

    override fun getDetailed(id: Int): Game {
        return Game(-1, "dummy",0,false,false)
    }

    override fun getAll(): ArrayList<Game> {
        val games = ArrayList<Game>()
        val con = db.open()
        try{
            val stmt =  con.prepareStatement("select * from game where active = true")
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


    /**
     * Does not actually delete the game only sets active to false, this is due to not having game sessions with game_id null.
     */
    override fun delete(id: Int): Boolean{
        val con = db.open()
        try{
            val stmt = con.prepareStatement("update game set active = false where game_id = ?")
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