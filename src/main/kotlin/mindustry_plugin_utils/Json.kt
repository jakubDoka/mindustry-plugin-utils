package mindustry_plugin_utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import java.lang.Boolean.parseBoolean
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.lang.Long.parseLong

class Json(body: String) {
    private val reg = body.trim()
    private val obj: Any = if(reg.startsWith("[")) Klaxon().parseJsonArray(reg.reader()) else Klaxon().parseJsonObject(reg.reader())

    override fun toString(): String {
        return when(obj) {
            is JsonArray<*> -> obj.toJsonString(true)
            is JsonObject -> obj.toJsonString(true)
            else -> "{}"
        }
    }

    fun modify(method: Method, type: Type, path: String, value: String): String {
        val obj = reach(path) ?: return "unreachable"
        val v = type.parse(value) ?: if(type == Type.Null) null else return "notParsable"
        val f = path.substring(path.lastIndexOf('.') + 1)

        when(obj) {
            is JsonArray<*> -> try {
                @Suppress("UNCHECKED_CAST")
                val arr = (obj as JsonArray<Any?>)
                val i = parseInt(f)
                when(method) {
                    Method.Set -> arr[i] = v
                    Method.Insert -> arr.add(i, v)
                    Method.Remove -> arr.removeAt(i)
                }
            } catch(e: Exception) {
                return "index"
            }
            is JsonObject -> when(method) {
                Method.Remove -> obj.remove(f)
                else -> obj[f] = v
            }
        }

        return ""
    }

    fun reach(path: String): Any? {
        val i = path.lastIndexOf('.')
        if (i == -1) return obj
        var top: Any? = obj
        for(part in path.substring(0, i).split(".")) {
            top = when(top) {
                is JsonObject -> top[part]
                is JsonArray<*> -> try {
                    top[parseInt(part)]
                } catch (e: Exception) {
                    null
                }
                else -> null
            }
        }
        return top
    }

    enum class Method {
        Set, Insert, Remove
    }

    enum class Type {
        Obj, Arr, Str, Double, Long, Bool, Null;

        fun parse(input: String): Any? {
            return try {
                when(this) {
                    Obj -> JsonObject()
                    Arr -> JsonArray<Any?>()
                    Str -> input
                    Double -> parseDouble(input)
                    Long -> parseLong(input)
                    Bool -> parseBoolean(input)
                    Null -> null
                }
            } catch(e: Exception) {
                null
            }
        }
    }
}