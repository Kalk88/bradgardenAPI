import java.io.FileInputStream
import java.util.*

/**
 * Created by kalk on 5/29/17.
 */

data class Config(val ip:String, val port:Int, val dbUrl:String, val dbUser:String, val dbPass:String)

fun main(args: Array<String>){
    // server()
    localserver()
}

private fun server() {
    val properties = Properties()
    properties.load(FileInputStream("src/main/resources/server.properties"))
    val conf = Config(properties.getProperty("SERVERIP"),
            properties.getProperty("SERVERPORT").toInt(),
            properties.getProperty("DBURL"),
            properties.getProperty("DBUSER"),
            properties.getProperty("DBPASSWORD")
    )

    Server().start(conf)
}

private fun localserver(){
    val properties = Properties()
    properties.load(FileInputStream("src/main/resources/localserver.properties"))
    val conf = Config(properties.getProperty("SERVERIP"),
            properties.getProperty("SERVERPORT").toInt(),
            properties.getProperty("DBURL"),
            properties.getProperty("DBUSER"),
            properties.getProperty("DBPASSWORD")
    )

    Server().start(conf)
}