package mindustry_plugin_utils

import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.StringBuilder
import java.util.regex.Pattern

object Templates {
    val color = "(?<!\\[)\\[[^\\[.]*\\]"
    val rank = "[＜<].*[>＞]"
    val all = "($color|$rank)"

    val colorR = Pattern.compile(color)
    val rankR = Pattern.compile(rank)
    val allR = Pattern.compile(all)

    fun page(title: String, lines: Array<String>, cap: Int, index: Int, color :String = "orange", comment: String = ""): String {
        val count = lines.size / cap
        val max = if(count * cap < lines.size) count else count - 1
        val id = max(min(max, index - 1), 0)

        val sb = StringBuilder()
        if(comment.isNotEmpty()) sb.append(comment).append("\n\n")
        for(i in cap * id until min(cap*(id+1), lines.size)) {
            sb.append(lines[i]).append("\n")
        }

        return info(title + "[${id+1}/${max+1}]", sb.toString(), color)
    }

    fun info(title: String, body: String, color: String = "orange"): String {
        return "[$color]==$title==[]\n\n$body"
    }

    fun cleanEmotes(string: String): String {
        return rankR.matcher(string).replaceAll("")
    }

    fun cleanColors(string: String): String {
        return colorR.matcher(string).replaceAll("")
    }

    fun cleanName(name: String): String {
        return allR.matcher(name).replaceAll("")
    }

    fun Long.time(): String {
        val sec = this / 1000
        val min = sec / 60
        val hour = min / 60
        val days = hour / 24
        return String.format("%d:%02d:%02d:%02d", days % 365, hour % 24, min % 60, sec % 60)
    }

    fun transition(text: String, vararg colors: String, density: Int = 1): String {
        val pcs = Array(colors.size) { Color.parse(colors[it]) }
        var segment = text.length / (pcs.size - 1)
        if(segment * pcs.size < text.length) {
            segment++
        }
        val sb = StringBuilder()
        for(i in text.indices step density) {
            val index = i / segment
            sb
                .append("[${pcs[index].l(pcs[min(index+1, pcs.size-1)], (i - index * segment).toDouble()/segment).toHex()}]")
                .append(text.substring(i, min(i + density, text.length)))
        }

        return sb.toString()
    }

    class Color(
        val r: Double = 1.0,
        val g: Double = 1.0,
        val b: Double = 1.0,
    ) {
        companion object {
            fun parse(hex: String): Color {
                val h = if(hex.startsWith("#"))
                    hex.substring(1)
                else
                    hex



                return when(h.length) {
                    3 -> Color(
                        hexToByte(h.substring(0, 1)),
                        hexToByte(h.substring(1, 2)),
                        hexToByte(h.substring(2, 3))
                    )
                    6 -> Color(
                        hexToByte(h.substring(0, 2)),
                        hexToByte(h.substring(2, 4)),
                        hexToByte(h.substring(4, 6))
                    )
                    else -> Color()
                }

            }

            private fun hexToByte(str: String): Double {
                return when(str.length) {
                    1 -> hexValue(str[0]).toDouble() / 0x0f
                    2 -> (hexValue(str[1]).toInt() or hexValue(str[0]).toInt().shl(4)).toDouble() / 0xFF
                    else -> 1.0
                }
            }

            private fun hexValue(c: Char): Char {
                return when(c) {
                    in '0'..'9' -> (c - '0').toChar()
                    in 'a'..'f' -> (c - 'a' + 10).toChar()
                    in 'A'..'F' -> (c - 'A' + 10).toChar()
                    else -> 0.toChar()
                }
            }

            private fun doubleToHex(f: Double): String {
                val b = (f * 0xff).toInt()
                return String(byteArrayOf(hexValue(b shr 4), hexValue(b and 0x0f)))
            }

            private fun hexValue(c: Int): Byte {
                return when(c) {
                    in 0..9 -> (c + '0'.toInt()).toByte()
                    in 10..16 -> (c - 10 + 'a'.toInt()).toByte()
                    else -> '0'.toByte()
                }
            }

            private fun ld(a: Double, b: Double, t: Double): Double {
                return a + (b - a) * t
            }
        }

        fun l(b: Color, t: Double): Color {
            return Color(
                ld(r, b.r, t),
                ld(g, b.g, t),
                ld(this.b, b.b, t),
            )
        }

        fun toHex(): String {
            return "#" + doubleToHex(r) + doubleToHex(g) + doubleToHex(b)
        }
    }
}