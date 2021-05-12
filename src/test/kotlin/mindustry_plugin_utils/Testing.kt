package mindustry_plugin_utils

object Testing {
    fun <T> assert(a: T, b: T) {
        if (a != b) {
            println("$a != $b")
            assert(false)
        }
    }
}