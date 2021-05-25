package mindustry_plugin_utils

import arc.util.async.Threads.sleep
import discord4j.core.`object`.entity.Message
import mindustry_plugin_utils.discord.Handler
import org.junit.jupiter.api.Test;

class LoggerTest{

    private val logger = Logger("config.json")

    init {
        logger.config.kind = Logger.Kind.Log
    }

    @Test
    fun hello() {
        logger.run {
            throw Exception("huh ooo:")
        }
        logger.run {
            throw Exception("huh ooo:")
        }
    }

    @Test
    fun discord() {
        val h = Handler("Njg5NzQ3OTA4ODU3MTY3OTE0.XnHXzA.Y3vD6oDbcUq9lgcA_RGF6p8cLLI", commandChannel = "551880313161121884", loadChannels = mapOf(
            "a" to "551880313161121884",
            "ho" to "its not a valid snowflake"
        ))
        h.reg(object: Handler.Cmd("hello", "<f> [k] {l}", "says hello") {
            override fun run(message: Message, arguments: List<String>) {
                for(a in arguments) {
                    message.channel.block()?.createMessage("$a no")?.block()
                }

            }
        })
        h.launch()
    }
}