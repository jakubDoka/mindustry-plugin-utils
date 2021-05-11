package mindustry_plugin_utils

import com.beust.klaxon.Klaxon
import java.io.File
import java.lang.Exception

class Messenger(
    private val prefix: String = "Messenger",
    private val verboseMessage: String = "verbose is disabled",
    private val verbose: Boolean = false
) {
    fun verbose(r: () -> Unit) {
        if(verbose) r.invoke()
        else log(verboseMessage)
    }

    fun <T> log(value: T) {
        print("$prefix:: ")
        println(value)
    }
}