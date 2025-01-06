package days.aoc2016

import days.Day
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

class Day5 : Day(2016, 5) {
    override fun partOne(): Any {
        return calculatePartOne(inputString)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputString)
    }

    fun calculatePartOne(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        var serial = 0
        var result = ""

        while (result.length < 8) {
            md.update("$input${serial++}".toByteArray(UTF_8))
            val hash = md.digest().toHex()
            if (hash.startsWith("00000")) {
                result += hash[5]
            }
        }
        return result
    }

    fun calculatePartTwo(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        var serial = 0
        val result = CharArray(8) { '_' }

        while (result.any { it == '_' } ) {
            md.update("$input${serial++}".toByteArray(UTF_8))
            val hash = md.digest().toHex()
            if (hash.startsWith("00000") && (hash[5] in '0'..'7') && result[hash[5].digitToInt()] == '_') {
                result[hash[5].digitToInt()] = hash[6]
            }
        }
        return result.joinToString("")
    }
}

fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }