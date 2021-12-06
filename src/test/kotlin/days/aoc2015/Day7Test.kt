package days.aoc2015

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test

class Day7Test {

    private val day = Day7()

    @Test
    fun testPartOne() {
        assertThat(day.circuit.getGate("d").calculateOutput(), `is`(72u))
        assertThat(day.circuit.getGate("e").calculateOutput(), `is`(507u))
        assertThat(day.circuit.getGate("f").calculateOutput(), `is`(492u))
        assertThat(day.circuit.getGate("g").calculateOutput(), `is`(114u))
        assertThat(day.circuit.getGate("h").calculateOutput(), `is`(65412u))
        assertThat(day.circuit.getGate("i").calculateOutput(), `is`(65079u))
        assertThat(day.circuit.getGate("x").calculateOutput(), `is`(123u))
        assertThat(day.circuit.getGate("y").calculateOutput(), `is`(456u))
    }

    @Test
    fun testPartTwo() {
        assertThat(day.partTwo(), `is`(false))
    }
}
