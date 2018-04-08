import org.junit.Test
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
}