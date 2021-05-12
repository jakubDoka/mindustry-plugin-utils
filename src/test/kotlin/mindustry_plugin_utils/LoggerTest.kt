package mindustry_plugin_utils

import arc.util.async.Threads.sleep
import discord4j.core.`object`.entity.Message
import mindustry_plugin_utils.discord.Handler
import org.junit.jupiter.api.Test;

class LoggerTest{

    private val logger = Logger("config.json")

    @Test
    fun hello() {
        logger.run {
            throw Exception("huh ooo:")
        }
    }

    @Test
    fun discord() {
        val h = Handler("token")
        h.reg(object: Handler.Cmd("hello", "<f> [k] {l}", "says hello") {
            override fun run(message: Message, arguments: List<String>) {
                for(a in arguments) {
                    message.channel.block()?.createMessage("$a no")?.block()
                }

            }
        })
        h.launch()
        while(true) {sleep(100)}
    }
}