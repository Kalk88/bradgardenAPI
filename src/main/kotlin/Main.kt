
/**
 * Created by kalk on 5/29/17.
 */

data class Config(val ip:String, val port:Int, val dbUrl:String, val dbUser:String, val dbPass:String)

fun main(args: Array<String>){
    Server().start()
}