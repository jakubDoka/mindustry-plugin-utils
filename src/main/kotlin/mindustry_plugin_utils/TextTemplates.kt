package mindustry_plugin_utils

object Templates {
    fun info(title: String, body: String, color: String = "orange"): String {
        return "[$color]==$title==[]\n\n$body"
    }
}