package mindustry_plugin_utils

import org.junit.jupiter.api.*;

class LoggerTest{

    private val logger = Logger("config.json")

    @Test
    fun hello() {
        logger.run {
            throw Exception("huh ooo:")
        }
    }
}