package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day19Test {

    private val day = Day19()

    @Test
    fun testPartOne() {
        val replacements =
"""
H => HO
H => OH
O => HH    
""".trimIndent().lines()

        assertThat(day.calculatePartOne(replacements, "HOH"), `is`(4))
        assertThat(day.calculatePartOne(replacements, "HOHOHO"), `is`(7))
    }

    @Test
    fun testPartTwo() {
        val replacements =
                """
e => H
e => O
H => HO
H => OH
O => HH
""".trimIndent().lines()
        //assertThat(day.calculatePartTwo(replacements, "HOH"), `is`(3))
        //assertThat(day.calculatePartTwo(replacements, "HOHOHO"), `is`(6))
    }
}
