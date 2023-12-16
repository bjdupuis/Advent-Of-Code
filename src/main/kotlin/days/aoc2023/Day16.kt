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
        return calculateEnergizedTilesFromBeam(tiles, Point2d(0, 0) to Point2d.Directions.East)
    }

    fun calculatePartTwo(input: List<String>): Int {
        val tiles = CharArray2d(input)
        var max = 0
        for (x in tiles.columnIndices) {
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(x, 0) to Point2d.Directions.South))
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(x, tiles.maxRowIndex) to Point2d.Directions.North))
        }

        for (y in tiles.rowIndices) {
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(0, y) to Point2d.Directions.East))
            max = max(max, calculateEnergizedTilesFromBeam(tiles, startingBeam = Point2d(tiles.maxColumnIndex, y) to Point2d.Directions.West))
        }

        return max
    }

    private fun calculateEnergizedTilesFromBeam(tiles: CharArray2d, startingBeam: Pair<Point2d, Point2d.Directions>): Int {
        val energizedTiles = mutableSetOf<Point2d>()
        val activeBeams = mutableListOf(startingBeam)

        fun nextTileLocation(current: Point2d, direction: Point2d.Directions): Point2d? {
            return if ((current + direction.delta).isWithin(tiles)) {
                current + direction.delta
            } else {
                null
            }
        }

        val seenBeams = mutableSetOf<Pair<Point2d, Point2d.Directions>>()

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

                '/' -> if (beam.second == Point2d.Directions.East) {
                    nextTileLocation(beam.first, Point2d.Directions.North)?.let {
                        activeBeams.add(it to Point2d.Directions.North)
                    }
                } else if (beam.second == Point2d.Directions.West) {
                    nextTileLocation(beam.first, Point2d.Directions.South)?.let {
                        activeBeams.add(it to Point2d.Directions.South)
                    }
                } else if (beam.second == Point2d.Directions.North) {
                    nextTileLocation(beam.first, Point2d.Directions.East)?.let {
                        activeBeams.add(it to Point2d.Directions.East)
                    }
                } else {
                    nextTileLocation(beam.first, Point2d.Directions.West)?.let {
                        activeBeams.add(it to Point2d.Directions.West)
                    }
                }

                '\\' -> if (beam.second == Point2d.Directions.East) {
                    nextTileLocation(beam.first, Point2d.Directions.South)?.let {
                        activeBeams.add(it to Point2d.Directions.South)
                    }
                } else if (beam.second == Point2d.Directions.West) {
                    nextTileLocation(beam.first, Point2d.Directions.North)?.let {
                        activeBeams.add(it to Point2d.Directions.North)
                    }
                } else if (beam.second == Point2d.Directions.North) {
                    nextTileLocation(beam.first, Point2d.Directions.West)?.let {
                        activeBeams.add(it to Point2d.Directions.West)
                    }
                } else {
                    nextTileLocation(beam.first, Point2d.Directions.East)?.let {
                        activeBeams.add(it to Point2d.Directions.East)
                    }
                }

                '|' -> if (beam.second == Point2d.Directions.North || beam.second == Point2d.Directions.South) {
                    nextTileLocation(beam.first, beam.second)?.let {
                        activeBeams.add(it to beam.second)
                    }
                } else {
                    nextTileLocation(beam.first, Point2d.Directions.North)?.let {
                        activeBeams.add(it to Point2d.Directions.North)
                    }
                    nextTileLocation(beam.first, Point2d.Directions.South)?.let {
                        activeBeams.add(it to Point2d.Directions.South)
                    }
                }

                '-' -> if (beam.second == Point2d.Directions.East || beam.second == Point2d.Directions.West) {
                    nextTileLocation(beam.first, beam.second)?.let {
                        activeBeams.add(it to beam.second)
                    }
                } else {
                    nextTileLocation(beam.first, Point2d.Directions.East)?.let {
                        activeBeams.add(it to Point2d.Directions.East)
                    }
                    nextTileLocation(beam.first, Point2d.Directions.West)?.let {
                        activeBeams.add(it to Point2d.Directions.West)
                    }
                }
            }
        }

        return energizedTiles.count()
    }

}