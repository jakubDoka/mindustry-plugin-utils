package mindustry_plugin_utils

import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.Test

class JsonTest {

    @Test
    fun test() {
        val json = Json(Klaxon().toJsonString(SomeJson()))

        json.modify(Json.Method.Set, Json.Type.Bool, "someBool", "true")

        println(json.toString())

        json.modify(Json.Method.Set, Json.Type.Obj, "left", "")
        json.modify(Json.Method.Insert, Json.Type.Long, "left.hello", "1000")

        println(json.toString())
    }

    class SomeJson(
        val someString: String = "",
        val someInt: Int = 0,
        val someBool: Boolean = false,
        val someJsonArray: Array<SomeJson> = arrayOf(),
        val someArray: Array<Int> = arrayOf(),
        val left: SomeJson? = null,
        val right: SomeJson? = null
    )
}