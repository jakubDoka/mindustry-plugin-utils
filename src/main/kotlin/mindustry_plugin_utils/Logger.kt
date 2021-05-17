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
    var config: Config
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
            Fs.createDefault(configRelativePath, config)
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

    fun run(type: Any, listener: () -> Unit) {
        Events.run(type) {
            try {
                listener.invoke()
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    fun <T> on(kind: Class<T>, listener: (T) -> Unit) {
        Events.on(kind) {
            try {
                listener.invoke(it)
            } catch (e: Exception) {
                ex(e)
            }
        }
    }

    private fun ex(t: Throwable) {
        try {
            val time = time()
            val f = when (config.kind) {
                Kind.Log -> {
                    Paths.get(config.output, time.substring(0, time.lastIndexOf("/"))).toFile()
                }
                else -> {
                    Paths.get(config.output, time).toFile()
                }
            }


            if (!f.exists()) {
                f.parentFile.mkdirs()
                f.createNewFile()
            }

            val ex = exToString(t)

            f.setWritable(true)
            f.appendText(">>> [$time]\n")
            f.appendText(ex)

           messenger.verbose {
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
            sb.append("\t").append(st).append("\n")
        }

        return sb.toString()
    }

    private fun time(): String {
        return format.format(Date(Time.millis()))
    }

    class Config(
        val output: String = "logOutput",
        var kind: Kind = Kind.Default,
        val time_format: String = "yyyy-MM-dd/hh/mm-ss-SSS",
        val verbose: Boolean = false
    )

    enum class Kind {
        Default, Log
    }
}