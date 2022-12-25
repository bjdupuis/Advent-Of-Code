package days.aoc2022

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day25Test {
    val day = Day25()
    val input = """
1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122
    """.trimIndent().lines().filter { it.isNotEmpty() }

        /*
        1=-0-2     1747
 12111      906
  2=0=      198
    21       11
  2=01      201
   111       31
 20012     1257
   112       32
 1=-1=      353
  1-12      107
    12        7
    1=        3
   122       37

        1              1
        2              2
        3             1=
        4             1-
        5             10
        6             11
        7             12
        8             2=
        9             2-
       10             20
       15            1=0
       20            1-0
     2022         1=11-2
    12345        1-0---0
314159265  1121-1110-1=0


         */

    @Test
    fun testPartOne() {
        MatcherAssert.assertThat(Snafu(input[0]).toLong(), `is`(1747))
        MatcherAssert.assertThat(Snafu(input[1]).toLong(), `is`(906))
        MatcherAssert.assertThat(Snafu(input[2]).toLong(), `is`(198))
        MatcherAssert.assertThat(Snafu(input[3]).toLong(), `is`(11))
        MatcherAssert.assertThat(Snafu(input[4]).toLong(), `is`(201))
        MatcherAssert.assertThat(Snafu(input[5]).toLong(), `is`(31))
        MatcherAssert.assertThat(Snafu(input[6]).toLong(), `is`(1257))
        MatcherAssert.assertThat(Snafu(input[7]).toLong(), `is`(32))
        MatcherAssert.assertThat(Snafu(input[8]).toLong(), `is`(353))
        MatcherAssert.assertThat(Snafu(input[9]).toLong(), `is`(107))
        MatcherAssert.assertThat(Snafu(input[10]).toLong(), `is`(7))
        MatcherAssert.assertThat(Snafu(input[11]).toLong(), `is`(3))
        MatcherAssert.assertThat(Snafu(input[12]).toLong(), `is`(37))

        MatcherAssert.assertThat(1L.toSnafu().value, `is`("1"))
        MatcherAssert.assertThat(2L.toSnafu().value, `is`("2"))
        MatcherAssert.assertThat(3L.toSnafu().value, `is`("1="))
        MatcherAssert.assertThat(4L.toSnafu().value, `is`("1-"))
        MatcherAssert.assertThat(5L.toSnafu().value, `is`("10"))
        MatcherAssert.assertThat(6L.toSnafu().value, `is`("11"))
        MatcherAssert.assertThat(7L.toSnafu().value, `is`("12"))
        MatcherAssert.assertThat(8L.toSnafu().value, `is`("2="))
        MatcherAssert.assertThat(9L.toSnafu().value, `is`("2-"))
        MatcherAssert.assertThat(10L.toSnafu().value, `is`("20"))
        MatcherAssert.assertThat(15L.toSnafu().value, `is`("1=0"))
        MatcherAssert.assertThat(20L.toSnafu().value, `is`("1-0"))
        MatcherAssert.assertThat(1257L.toSnafu().value, `is`("20012"))
        MatcherAssert.assertThat(2022L.toSnafu().value, `is`("1=11-2"))
        MatcherAssert.assertThat(12345L.toSnafu().value, `is`("1-0---0"))
        MatcherAssert.assertThat(314159265L.toSnafu().value, `is`("1121-1110-1=0"))

        MatcherAssert.assertThat(day.calculateSumInSnafu(input), `is`("2=-1=0"))
    }
}