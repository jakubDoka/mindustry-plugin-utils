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
            f.writeText(Klaxon().toJsonString(value))
        } catch (e: Exception) {
            println("Failed to create default config with path $path.")
            e.printStackTrace()
        }
    }
}