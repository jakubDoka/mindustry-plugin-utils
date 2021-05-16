package mindustry_plugin_utils

import com.beust.klaxon.Klaxon
import java.io.File

object Fs {
    fun createDefault(path: String, value: Any) {
        try {
            val f = File(path)
            if (f.exists()) return
            f.parentFile.mkdirs()
            f.createNewFile()
            f.setWritable(true)
            f.writeText(value.jsonToString())
        } catch (e: Exception) {
            println("Failed to create default config with path $path.")
            e.printStackTrace()
        }
    }

    fun Any.jsonToString(prettyPrint: Boolean = true): String{
        var thisJsonString = Klaxon().toJsonString(this)
        var result = thisJsonString
        if(prettyPrint) {
            result = if(thisJsonString.startsWith("[")){
                Klaxon().parseJsonArray(thisJsonString.reader()).toJsonString(true)
            } else {
                Klaxon().parseJsonObject(thisJsonString.reader()).toJsonString(true)
            }
        }
        return result
    }
}