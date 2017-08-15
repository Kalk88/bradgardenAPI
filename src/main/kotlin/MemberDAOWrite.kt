import org.apache.commons.dbutils.DbUtils

class MemberDAOWrite : DAOWriteInterface {

    override fun add(obj: Any): Int {
        var id: Int
        val member = obj as addMember

        val con = DBConnection.instance.open()
        try {
            val stmt = con.prepareStatement("insert into member (first_name, last_name) values (?,?) returning member_id")
            stmt.setString(1, member.firstName)
            stmt.setString(2,member.lastName)
            stmt.executeQuery()
            stmt.resultSet.next()
            id = stmt.resultSet.getInt(1)
        } catch (e: Exception) {
            throw APIException("could not add member $member.firstName $member.lastName")
        } finally {
            DbUtils.close(con)
        }
        return id
    }

    override fun update(id: Int, obj: Any): Boolean {
        val con = DBConnection.instance.open()
        val member = obj as addMember
        try {
            val stmt = con.prepareStatement("update Member set first_name = ?, last_name = ? where member_id = ?")
            stmt.setString(1, member.firstName)
            stmt.setString(2, member.lastName)
            stmt.setInt(3, id)
            stmt.execute()
            MemberDAO.logger.info("Updated member $id")
            return true
        } catch (e: Exception) {
            throw APIException("could not update member $member.firstName $member.lastName")
        } finally {
            DbUtils.close(con)
        }
    }

    override fun delete(id: Int): Boolean {
        val con = DBConnection.instance.open()
        try {
            val stmt = con.prepareStatement("delete from member where member_id = ?")
            stmt.setInt(1, id)
            stmt.execute()
            MemberDAO.logger.info("Removed member $id")
            return true
        } catch (e: Exception) {
            throw APIException("Failed to delete $id")
        } finally {
            DbUtils.close(con)
        }
    }
}