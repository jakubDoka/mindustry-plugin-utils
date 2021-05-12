package mindustry_plugin_utils

import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.StringBuilder

object Templates {
    fun page(title: String, lines: Array<String>, cap: Int, index: Int, color :String = "orange", comment: String = ""): String {
        val count = lines.size / cap
        val max = if(count * cap < lines.size) count else count + 1
        val id = max(min(max, index - 1), 0)

        val sb = StringBuilder()
        if(comment.isNotEmpty()) sb.append(comment).append("\n\n")
        for(i in cap * id until min(cap*(id+1), lines.size)) {
            sb.append(lines[i]).append("\n")
        }

        return info(title, sb.toString(), color)
    }

    fun info(title: String, body: String, color: String = "orange"): String {
        return "[$color]==$title==[]\n\n$body"
    }
}