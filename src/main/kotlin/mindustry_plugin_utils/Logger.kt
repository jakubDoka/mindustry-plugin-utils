package mindustry_plugin_utils

import arc.Events
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
        try {
            config =  Klaxon().parse<Config>(File(configRelativePath))!!
        } catch (e: Exception) {
            config = Config()
            log("error when opening config file: " + e.message)
            verbose {
                e.printStackTrace()
            }
        }

        format = SimpleDateFormat(config.time_format)
    }

    fun run(r: Runnable) {
        try {
            r.run()
        } catch (e: Exception) {
            ex(e)
        }
    }

    fun <T> run(type: Class<T>, listener: (T) -> Unit) {
        Events.on(type) {
            try {
                listener.invoke(it)
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    fun <T> fire(kind: Class<*>, listener: () -> Unit) {
        Events.fire(kind) {
            try {
                listener.invoke()
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    private fun ex(t: Throwable) {
        val time = time()
        val f = when (config.type) {
            "log" -> {
                Path.of(config.output, time.substring(0, time.lastIndexOf("/"))).toFile()
            }
            else -> {
                Path.of(config.output, t.message, time).toFile()
            }
        }


        if(!f.exists()) {
            f.mkdirs()
            f.createNewFile()
        }

        val ex = exToString(t)

        f.appendText(">>> [$time]\n")
        f.appendText(ex)

        if(config.print) {
            println(ex)
        }
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


    private fun verbose(r: () -> Unit) {
        if(config.verbose) r.invoke()
        else log("Add '\"verbose\": true' to config file to see the stacktrace.")
    }

    private fun <T> log(value: T) {
        print("Logger:: ")
        println(value)
    }


    class Config(
        val output: String = "logOutput",
        val type: String = "default",
        val time_format: String = "yyyy-MM-dd/hh/mm:ss:SSS",
        val verbose: Boolean = false,
        val print: Boolean = true
    )
}