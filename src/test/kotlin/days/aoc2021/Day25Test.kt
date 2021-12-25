package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day25Test {
    private val day = Day25()

    @Test
    fun testPartOne() {
        val inputList = """
            v...>>.vv>
            .vv>>.vv..
            >>.>v>...v
            >>v>>.>.v.
            v>v.vv.v..
            >.>>..v...
            .vv..>.>v.
            v.v..>>v.v
            ....v..v.>
        """.trimIndent().lines()

        MatcherAssert.assertThat(day.calculateFirstStepWhereCucmbersDontMove(inputList), `is`(58))
    }

}