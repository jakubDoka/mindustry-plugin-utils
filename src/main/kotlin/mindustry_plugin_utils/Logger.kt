package mindustry_plugin_utils

import arc.Events
import arc.func.Cons
import com.beust.klaxon.Klaxon
import java.io.File
import arc.util.Time
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*

class Logger(configRelativePath: String) {
    private var config: Config
    private var format: SimpleDateFormat

    init {
        config = Klaxon().parse<Config>(File(configRelativePath)) ?: Config()
        format = SimpleDateFormat(config.time_format)
    }

    fun <T> run(type: Class<T>, listener: Cons<T>) {
        Events.on(type) {
            try {
                listener.get(it)
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    fun <T> fire(kind: Class<*>, listener: Runnable) {
        Events.fire(kind) {
            try {
                listener.run()
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    fun ex(t: Throwable) {
        val time = time()
        val f = when (config.type) {
            "log" -> {
                Path.of(config.output, time.substring(0, time.lastIndexOf("/"))).toFile()
            }
            else -> {
                Path.of(config.output, time).toFile()
            }
        }


        if(!f.exists()) {
            f.mkdirs()
            f.createNewFile()
        }

        f.appendText(">>> [$time]\n")
        f.appendText(exToString(t))
    }

    private fun exToString(t: Throwable): String {
        val sb = StringBuilder(t.message + "\n")
        for(st in t.stackTrace) {
            sb.append(st).append("\n")
        }

        return sb.toString()
    }

    private fun time(): String {
        return format.format(Date(Time.millis()))
    }


    class Config(
        val output: String = "logOutput",
        val type: String = "default",
        val time_format: String = "yyyy-MM-dd/hh/mm:ss:fff"
    )
}