package days.aoc2021

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test

class Day18Test {
    private val day = Day18()

    @Test
    fun testPartOne() {
        /*
        MatcherAssert.assertThat(day.parseSnailfishNumber("[1,2]"), `is`(Day18.SnailfishNumber.Pair(
            Day18.SnailfishNumber.Regular(1),
            Day18.SnailfishNumber.Regular(2))))
        MatcherAssert.assertThat(day.parseSnailfishNumber("[[1,2],3]"), `is`(
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Pair(Day18.SnailfishNumber.Regular(1), Day18.SnailfishNumber.Regular(2)),
            Day18.SnailfishNumber.Regular(3))))
        MatcherAssert.assertThat(day.parseSnailfishNumber("[1,[2,3]]"), `is`(
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Regular(1),
                Day18.SnailfishNumber.Pair(Day18.SnailfishNumber.Regular(2),
                    Day18.SnailfishNumber.Regular(3)))))
        MatcherAssert.assertThat(day.parseSnailfishNumber("[[1,2],[3,4]]"), `is`(
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Pair(
                    Day18.SnailfishNumber.Regular(1),
                    Day18.SnailfishNumber.Regular(2)),
                Day18.SnailfishNumber.Pair(
                    Day18.SnailfishNumber.Regular(3),
                    Day18.SnailfishNumber.Regular(4)))))

        val one = Day18.SnailfishNumber.Pair(
            Day18.SnailfishNumber.Regular(1),
            Day18.SnailfishNumber.Regular(2)
        )
        val two = Day18.SnailfishNumber.Pair(
            Day18.SnailfishNumber.Regular(3),
            Day18.SnailfishNumber.Regular(4)
        )
        val sum = Day18.SnailfishNumber.Pair(
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Regular(1),
                Day18.SnailfishNumber.Regular(2)
            ),
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Regular(3),
                Day18.SnailfishNumber.Regular(4)
            )
        )

        MatcherAssert.assertThat(one.plus(two), `is`(sum))

        val input1 =
            """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            """.trimIndent().lines()
        val summed = Day18.SnailfishNumber.Pair(
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Pair(
                    Day18.SnailfishNumber.Pair(
                            Day18.SnailfishNumber.Regular(1),
                            Day18.SnailfishNumber.Regular(1)
                    ),
                    Day18.SnailfishNumber.Pair(
                        Day18.SnailfishNumber.Regular(2),
                        Day18.SnailfishNumber.Regular(2)
                    )
                ),
                Day18.SnailfishNumber.Pair(
                        Day18.SnailfishNumber.Regular(3),
                        Day18.SnailfishNumber.Regular(3))
            ),
            Day18.SnailfishNumber.Pair(
                Day18.SnailfishNumber.Regular(4),
                Day18.SnailfishNumber.Regular(4)
            )
        )

        MatcherAssert.assertThat(day.addSnailfishNumbersTogether(input1), `is`(summed))
*/

        MatcherAssert.assertThat(day.parseSnailfishNumber("[[[[[9,8],1],2],3],4]").reduce(), `is`(day.parseSnailfishNumber("[[[[0,9],2],3],4]")))
        MatcherAssert.assertThat(day.parseSnailfishNumber("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]").reduce(), `is`(day.parseSnailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")))
        MatcherAssert.assertThat(day.parseSnailfishNumber("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]").reduce(), `is`(day.parseSnailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")))

        MatcherAssert.assertThat(day.parseSnailfishNumber("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]").magnitude(), `is`(4140))
    }

    @Test
    fun testPartTwo() {
        val input = """
[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
""".trimIndent().lines()

        MatcherAssert.assertThat(day.parseSnailfishNumber("[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]")
            .plus(day.parseSnailfishNumber("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]")).reduce(),
            `is`(day.parseSnailfishNumber("[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]")))

        MatcherAssert.assertThat(day.parseSnailfishNumber("[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]").magnitude(), `is`(3993))
        MatcherAssert.assertThat(day.findLargestMagnitudeOfPairs(input), `is`(3993))
    }
}