const val dateString = "1997-07-16T19:20:30.45+01:00";

fun dummyMember(id: Int, first: String = "Anthony", last: String = "Dankfano"): Member {
    return Member(id, first, last, 100, 1.0, 0, 0, 100)
}

fun dummyGame(id: Int, name: String = "dummy"): Game {
    return Game(id, name, 100, true, true)
}

fun dummySession(id: Int): Session {
    return Session(id, dateString, 1, listOf(1), listOf(3, 2), listOf(1))
}