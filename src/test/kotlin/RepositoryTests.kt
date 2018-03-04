import org.junit.Before

class RepositoryTests {
    lateinit var repo:Repository

    @Before
    fun setup() {
        repo = Repository(TestDB.instance)
    }


}