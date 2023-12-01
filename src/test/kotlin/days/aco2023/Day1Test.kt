package days.aco2023

import days.aoc2023.Day1
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day1Test {
    val day = Day1()
    val input1 = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet        
    """.trimIndent().lines()
    val input2 = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePartOne(input1), Is.`is`(142))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePartTwo(input2), Is.`is`(281))
    }
}