class Repository(val db: Database) {
    val memberDAO = MemberDAO(db)
    val gameDAO = GameDAO(db)
    val sessionDAO = SessionDAO(db)
    var members = memberDAO.getAll()
    var games = gameDAO.getAll()
    var sessions = sessionDAO.getAll()
}