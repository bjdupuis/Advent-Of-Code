package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day24Test {
    private val day = Day24()

    @Test
    fun testPartOne() {
        val instructions1 = """
            inp x
            mul x -1
        """.trimIndent().lines()
        var alu = Day24.ALU(instructions1)
        MatcherAssert.assertThat(alu.processInstructionsWithInput("4").registers["x"], `is`(-4))

        val instructions2 = """
            inp z
            inp x
            mul z 3
            eql z x
        """.trimIndent().lines()
        alu = Day24.ALU(instructions2)
        MatcherAssert.assertThat(alu.processInstructionsWithInput("39").registers["z"], `is`(1))
        alu.reset()
        MatcherAssert.assertThat(alu.processInstructionsWithInput("37").registers["z"], `is`(0))
    }
}