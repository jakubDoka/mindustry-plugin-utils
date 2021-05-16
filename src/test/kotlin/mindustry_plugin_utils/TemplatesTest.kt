package mindustry_plugin_utils

import mindustry_plugin_utils.Templates.info
import mindustry_plugin_utils.Templates.page
import mindustry_plugin_utils.Testing
import org.junit.jupiter.api.Test

class TemplatesTest {

    @Test
    fun pageTest() {
        val arr = Array(20) { it.toString() }
        Testing.assert(page("a", arr, 5, 1), info("a", "0\n1\n2\n3\n4\n"))
        Testing.assert(page("a", arr, 5, 4), info("a", "15\n16\n17\n18\n19\n"))
        Testing.assert(page("a", arr, 7, 4), info("a", "14\n15\n16\n17\n18\n19\n"))
    }

    @Test
    fun transition() {
        println(Templates.transition("and this works too", "fff", "f0f", "0f0", density = 3))
    }
}