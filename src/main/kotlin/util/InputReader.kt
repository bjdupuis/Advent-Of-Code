package util

import java.io.File

object InputReader {

    fun getInputAsString(year: Int, day: Int): String {
        return fromResources(year, day).readText()
    }

    fun getInputAsList(year: Int, day: Int): List<String> {
        return fromResources(year, day).readLines()
    }

    private fun fromResources(year: Int, day: Int): File {
        return File(javaClass.classLoader.getResource("$year/input_day_$day.txt").toURI())
    }
}
