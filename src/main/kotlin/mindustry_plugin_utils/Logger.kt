package mindustry_plugin_utils

import arc.Events
import com.beust.klaxon.Klaxon
import java.io.File
import arc.util.Time
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class Logger(configRelativePath: String) {
    private var config: Config
    private var format: SimpleDateFormat
    private lateinit var messenger: Messenger

    init {
        try {
            config =  Klaxon().parse<Config>(File(configRelativePath))!!
            initMessenger(config.verbose)
        } catch (e: Exception) {
            config = Config()
            initMessenger(false)
            messenger.log("error when opening config file: " + e.message)
            messenger.verbose {
                e.printStackTrace()
            }
        }

        format = SimpleDateFormat(config.time_format)
    }

    private fun initMessenger(verbose: Boolean) {
        messenger = Messenger("Logger", "add '\"verbose\": true' to config to see error messages", verbose)
    }

    fun run(r: () -> Unit) {
        try {
            r.invoke()
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

    fun <T> on(kind: Class<*>, listener: () -> Unit) {
        Events.fire(kind) {
            try {
                listener.invoke()
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    private fun ex(t: Throwable) {
        try {
            val time = time()
            var f = when (config.type) {
                "log" -> {
                    Paths.get(config.output, time.substring(0, time.lastIndexOf("/"))).toFile()
                }
                else -> {
                    Paths.get(config.output, t.message, time).toFile()
                }
            }


            if (!f.exists()) {
                f.mkdirs()
                f = File(f, "lig.txt")
                f.createNewFile()
                messenger.log(f.exists())
                messenger.log(f.absolutePath)

            }

            val ex = exToString(t)

            f.setWritable(true)
            f.appendText(">>> [$time]\n")
            f.appendText(ex)

            if (config.print) {
                println(ex)
            }
        } catch (e: Exception) {
            messenger.log("Unable to create file.")
            e.printStackTrace()
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

    class Config(
        val output: String = "logOutput",
        val type: String = "default",
        val time_format: String = "yyyy-MM-dd/hh/mm-ss-SSS",
        val verbose: Boolean = false,
        val print: Boolean = true
    )
}