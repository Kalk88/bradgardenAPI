import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataclassTests {

    @Test fun should_return_Member_name_in_titleCase() {
        val member = dummyMember(1, "teZtOny", "spanKFANO")
        assertTrue(member.firstName == "Teztony")
        assertTrue(member.lastName == "Spankfano")

        val member2 = dummyMember(2, "teZtOny johnny", "spanKFANO")
        assertTrue(member2.firstName == "Teztony Johnny")
        assertTrue(member2.lastName == "Spankfano")
    }

    @Test fun should_return_Game_name_in_titleCase() {
        val game = dummyGame(1, "the liFE Of LEIf")
        assertTrue(game.name == "The Life Of Leif")
    }

    @Test fun should_create_valid_dateString() {
        val session = Session(-1, null, 1, listOf(1), listOf(3, 2), listOf(1))
        val tz = TimeZone.getTimeZone("Europe/Copenhagen")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ")
        df.timeZone = tz
        val date = df.format(Date())
        assertEquals(date, session.date, "should be equal, can fail if hh/mm just about to change")
    }

}