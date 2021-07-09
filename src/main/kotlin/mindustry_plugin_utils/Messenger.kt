package mindustry_plugin_utils

open class Messenger(private val prefix: String) {

    fun <T> log(value: T) {
        print("$prefix::")
        println(value)
    }
}