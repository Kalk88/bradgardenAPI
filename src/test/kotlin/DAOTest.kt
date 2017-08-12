
import dao.DAO
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.*
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import kotlin.test.assertEquals

/**
 * Created by kalk on 8/11/17.
 */

class DAOTest {

    @Test fun createDAO() {
        val dao = DAO()
        assertThat(dao, instanceOf(DAO::class.java))
    }

    @Test fun addToDAO() {
        val dao = DAO()
        assertThat(dao.add(addMember("jens", "test")), instanceOf(Int::class.java))
    }

    @Test fun updateInDAO() {
        val dao = DAO()
        assertTrue(dao.update(1))
    }

    @Test fun getFromDAO() {
        val dao = DAO()
        assertEquals(dao.get(), ArrayList<Any>())
    }

    @Test fun deleteFromDAO() {
        val dao = DAO()
        assertTrue(dao.delete(1))
    }
}


