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

    fun clean(string: String, begin: String?, end: String?): String {
        var string = string
        var fromBegin = 0
        var fromEnd = 0
        while (string.contains(begin!!)) {
            val first = string.indexOf(begin, fromBegin)
            val last = string.indexOf(end!!, fromEnd)
            if (first == -1 || last == -1) break
            if (first > last) {
                fromBegin = first + 1
                fromEnd = last + 1
            }
            string = string.substring(0, first) + string.substring(last + 1)
        }
        return string
    }

    fun cleanEmotes(string: String): String {
        return clean(string, "<", ">")
    }

    fun cleanColors(string: String): String {
        return clean(string, "[", "]")
    }

    fun cleanName(name: String): String? {
        return cleanEmotes(cleanColors(name)).replace(" ", "_")
    }

    fun Long.time(): String {
        val sec = this / 1000
        val min = sec / 60
        val hour = min / 60
        val days = hour / 24
        return String.format("%d:%02d:%02d:%02d", days % 365, hour % 24, min % 60, sec % 60)
    }
}