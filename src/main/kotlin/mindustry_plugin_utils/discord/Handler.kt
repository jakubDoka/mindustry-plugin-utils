package mindustry_plugin_utils.discord

import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.message.MessageCreateEvent
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Handler(
    val token: String,
    val prefix: String = "!",
    val notKnowMessage: String = "I don't know this command.",
): HashMap<String, Handler.Cmd>() {
    val client = DiscordClient.create(token)
    val gateway = client.login().block()!!

    fun launch() {
        gateway.on(MessageCreateEvent::class.java).subscribe{
            val message = it.message
            if(!message.content.startsWith(prefix) || message.content.length <= prefix.length || !message.content[prefix.length].isLetter() || message.author.get().isBot) {
                return@subscribe
            }

            val space = message.content.indexOf(' ')
            val commandName = message.content.substring(prefix.length, if(space == -1) message.content.length else space)
            val c = get(commandName)
            if (c == null) {
                message.channel.block()?.createMessage(notKnowMessage)?.block()
                return@subscribe
            }

            val arguments = message.content.split(" ", limit = c.maxArgs + 1)
            val resp = c.check(message, arguments.size)
            if(resp != "") {
                message.channel.block()?.createMessage(resp)?.block()
                return@subscribe
            }

            c.run(message, arguments.subList(1, arguments.size))
        }
        gateway.onDisconnect().block()
    }


    fun reg(cmd: Cmd) {
        put(cmd.name, cmd)
    }

    abstract class Cmd(val name: String, val args: String, val description: String) {
        var maxArgs = 0
        var minArgs = 0
        var attachment = false
        var permissions = HashSet<String>()

        init {
            val parts = args.split(" ").filter{ it.isNotEmpty() }
            for(p in parts) {
                if(p.first() == '<' && p.last() == '>') {
                    if(attachment || minArgs != maxArgs) {
                        throw IllegalArgumentException("there cannot be any obligatory argument after optional or attachment")
                    }
                    minArgs++
                    maxArgs++
                } else if(p.first() == '[' && p.last() == ']') {
                    maxArgs++
                } else if(p.first() == '{' && p.last() == '}') {
                    attachment = true
                } else {
                    throw IllegalArgumentException("There cannot be any spaces within argument definitions. Only tree types are allowed '<>', '[]' and '{}'")
                }
            }
        }

        fun check(message: Message, argumentCount: Int): String {
            if(permissions.isNotEmpty()) {
                val list = message.authorAsMember.block()?.roles?.collectList()?.block() ?: return "Unable to verify what roles you have. You are too dangerous."
                var found = false
                for (i in list) {
                    if (permissions.contains(i.name)) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    return "You don't have permission to use this command. You need one of ${Arrays.toString(permissions.toArray())}."
                }
            }

            if(attachment && message.attachments.isEmpty()) {
                return "You are missing an attachment. structure: ${args}\""
            }

            if (argumentCount < minArgs) {
                return "Too few arguments. structure: ${args}\""
            }

            return ""
        }

        abstract fun run(message: Message, arguments: List<String>)
    }
}