package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day9Test {
    val day = Day9()
    val input1 = """
R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2        
    """.trimIndent().lines()

    val input2 = """
R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20        
    """.trimIndent().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.calculatePositionsTailVisits(input1,1), `is`(13))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.calculatePositionsTailVisits(input1,9), `is`(1))
        MatcherAssert.assertThat(day.calculatePositionsTailVisits(input2,9), `is`(36))
    }
}