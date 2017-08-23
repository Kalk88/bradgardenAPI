interface ControllerInterface {
    fun add(data: String): String
    fun update(id: String, data: String): String
    fun getFromParams(params: HashMap<String, String>): String
    fun getFromID(id: String): String
    fun removeWithID(id: String)

    fun parseParam(param: String?, default: Int): Int{
        return param?.toInt() ?: default
    }
}