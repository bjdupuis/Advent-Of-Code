package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test

class Day1Test {
    val day = Day1()
    val input = """
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000        
    """.trimIndent().trim().lines()


    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.highestCalorieElf(input), Is.`is`(24000))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.highestThreeCalorieElves(input), Is.`is`(45000))
    }

}