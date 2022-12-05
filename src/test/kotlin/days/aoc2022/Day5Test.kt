package days.aoc2022

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day5Test {
    val day = Day5()
    val input =
        """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
    """.trimEnd().lines()

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(day.determineTopOfStacksMovingOneCrateAtATime(input), `is`("CMZ"))
    }

    @Test
    fun testPartTwo() {
        MatcherAssert.assertThat(day.determineTopOfStacksMovingAllCratesAtOnce(input), `is`("MCD"))
    }
}