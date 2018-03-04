interface ControllerInterface <T> {
    fun add(data: T): String
    fun update(id: String, data: T): String
    fun getFromParams(params: HashMap<String, String>): String
    fun getFromID(id: String): String
    fun removeWithID(id: String)
    fun getAll(): ArrayList<T>

    fun paramOrDefault(param: Int, default: Int): Int {
        if (param <= 0)
            return default
        return param
    }
}