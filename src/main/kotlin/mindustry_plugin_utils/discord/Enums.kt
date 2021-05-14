package mindustry_plugin_utils.discord

import java.lang.StringBuilder

object Enums {
    fun <T : Enum<T>> contains(cl: Class<T>, value: String): Enum<T>? {
        for (v in cl.enumConstants) {
            if (v.name == value) return v
        }
        return null
    }

    // prints all enum variants
    fun <T : Enum<T>> list(cl: Class<T>): String {
        val s = StringBuilder()
        for (v in cl.enumConstants) {
            s.append(v).append(" ")
        }
        return s.substring(0, s.length - 2)
    }
}