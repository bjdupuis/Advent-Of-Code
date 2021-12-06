package days.aoc2020

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class Day20Test {

    private val day = Day20()

    @Test
    fun testPartOne() {
        assertThat(day.partOne(), `is`(20899048083289L))
    }

    @Test
    fun testPartTwo() {
        val tileString =
"""
Tile 3079:
#.#.#####.
.#..######
..#.......
######....
####.#..#.
.#...#.##.
#.#####.##
..#.###...
..#.......
..#.###...
    
""".trimIndent().lines()

        val rotated =
"""
...#.##..#
....###.#.
####.###.#
...#.##...
#.##..#.##
#.#####.##
#.##....##
....#...##
...###..##
...#....#.
""".trimIndent().lines()

        val flippedVert =
"""
...#....#.
...###..##
....#...##
#.##....##
#.#####.##
#.##..#.##
...#.##...
####.###.#
....###.#.
...#.##..#
""".trimIndent().lines()

        val tiles = day.parseTiles(tileString)

        val borders = listOf(2+4+8+16+32+128+512, 1+4+16+32+64+128+256,
                8+256, 2+64,
                4+16+32+64, 8+16+32+128,
                1+8+16+64, 8+32+64+512
                )

        val rotatedBorders = listOf(1+8+16+64, 8+32+64+512,
                2+4+8+16+32+128+512, 1+4+16+32+64+128+256,
                8+256, 2+64,
                4+16+32+64, 8+16+32+128
        )

        val flippedVertBorders = listOf(2+64, 8+256,
                1+4+16+32+64+128+256, 2+4+8+16+32+128+512,
                8+32+64+512, 1+8+16+64,
                8+16+32+128, 4+16+32+64
        )

        assertThat(tiles[0].borders(), `is`(borders))
        tiles[0].rotateRight()
        assertThat(tiles[0].borders(), `is`(rotatedBorders))

        var unmatched = tiles[0].image.mapIndexed { index, list ->
            val listToString = list.joinToString("")
            listToString == rotated[index]
        }
        assertThat(unmatched.all { it }, `is`(true))

        tiles[0].flipVertical()
        assertThat(tiles[0].borders(), `is`(flippedVertBorders))

        unmatched = tiles[0].image.mapIndexed { index, list ->
            val listToString = list.joinToString("")
            listToString == flippedVert[index]
        }

        assertThat(unmatched.all { it }, `is`(true))

        assertThat(day.partTwo(), `is`(273))
    }
}
