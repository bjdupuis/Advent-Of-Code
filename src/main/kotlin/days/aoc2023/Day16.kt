package days.aoc2023

import days.Day
import util.CharArray2d
import util.Point2d
import kotlin.math.max

class Day16 : Day(2023, 16) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val tiles = CharArray2d(input)
        return calculateEnergizedTilesFromBeam(tiles, Point2d(0, 0) to Point2d.Direction.East)
    }

    fun calculatePartTwo(input: List<String>): Int {
        val tiles = CharArray2d(input)
        var max = 0
        for (x in tiles.columnIndices) {
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(x, 0) to Point2d.Direction.South))
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(x, tiles.maxRowIndex) to Point2d.Direction.North))
        }

        for (y in tiles.rowIndices) {
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(0, y) to Point2d.Direction.East))
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(tiles.maxColumnIndex, y) to Point2d.Direction.West))
        }

        return max
    }

    private fun calculateEnergizedTilesFromBeam(tiles: CharArray2d, startingBeam: Pair<Point2d, Point2d.Direction>): Int {
        val energizedTiles = mutableSetOf<Point2d>()
        val activeBeams = mutableListOf(startingBeam)
        val seenBeams = mutableSetOf<Pair<Point2d, Point2d.Direction>>()

        fun nextTileLocation(current: Point2d, direction: Point2d.Direction): Point2d? {
            return if ((current + direction.delta).isWithin(tiles)) {
                current + direction.delta
            } else {
                null
            }
        }

        fun splitBeam(beam: Pair<Point2d, Point2d.Direction>, newDirections: Set<Point2d.Direction>) {
            if (beam.second in newDirections) {
                nextTileLocation(beam.first, beam.second)?.let {
                    activeBeams.add(it to beam.second)
                }
            } else {
                newDirections.forEach { direction ->
                    nextTileLocation(beam.first, direction)?.let {
                        activeBeams.add(it to direction)
                    }
                }
            }
        }

        val rightSlant = mapOf(
            Point2d.Direction.North to Point2d.Direction.East,
            Point2d.Direction.East to Point2d.Direction.North,
            Point2d.Direction.South to Point2d.Direction.West,
            Point2d.Direction.West to Point2d.Direction.South
        )
        val leftSlant = mapOf(
            Point2d.Direction.North to Point2d.Direction.West,
            Point2d.Direction.West to Point2d.Direction.North,
            Point2d.Direction.South to Point2d.Direction.East,
            Point2d.Direction.East to Point2d.Direction.South
        )
        fun turnBeam(beam: Pair<Point2d, Point2d.Direction>, map: Map<Point2d.Direction,Point2d.Direction>) {
            map[beam.second]?.let { newDirection ->
                nextTileLocation(beam.first, newDirection)?.let {
                    activeBeams.add(it to newDirection)
                }
            }
        }

        while (activeBeams.isNotEmpty()) {
            val beam = activeBeams.removeFirst()
            if (seenBeams.contains(beam)) {
                continue
            } else {
                seenBeams.add(beam)
            }
            energizedTiles.add(beam.first)

            when (tiles[beam.first]) {
                '.' -> nextTileLocation(beam.first, beam.second)?.let {
                    activeBeams.add(it to beam.second)
                }
                '/' -> turnBeam(beam, rightSlant)
                '\\' -> turnBeam(beam, leftSlant)
                '|' -> splitBeam(beam, setOf(Point2d.Direction.North, Point2d.Direction.South))
                '-' -> splitBeam(beam, setOf(Point2d.Direction.East, Point2d.Direction.West))
            }
        }

        return energizedTiles.count()
    }

}