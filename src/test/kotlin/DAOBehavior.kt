import org.junit.Assert.*
import org.junit.*

class DAOBehavior {
    @Test fun creates_MemberDAO() {
        val dao = MemberDAO()
        Assert.assertEquals(dao::class, MemberDAO::class)
    }

    @Test fun should_add_member_to_database() {
        val dao = MemberDAO()
        val member = addMember("jens", "test") //stub or mock?
        Assert.assertEquals(dao.add(member), Int::class)
    }

    @Test fun updateInMemberDAO() {
        val dao = MemberDAO()
        val member = addMember("jens", "test")
        Assert.assertTrue(dao.update(1, member))
    }

    @Test fun getFromMemberDAO() {
        val dao = MemberDAO()
        Assert.assertEquals(dao.get(), ArrayList<Any>())
    }

    @Test fun getDetailedFromMemberDAO() {
        val dao = MemberDAO()
        Assert.assertEquals(dao.getDetailed(1), Any::class)
    }

    @Test fun deleteFromMemberDAO() {
        val dao = MemberDAO()
        Assert.assertTrue(dao.delete(1))
    }
}