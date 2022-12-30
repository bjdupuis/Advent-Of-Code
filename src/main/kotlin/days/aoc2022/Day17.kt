package days.aoc2022

import days.Day
import util.Array2d
import util.Point2d
import util.Point2dl
import kotlin.math.max

class Day17 : Day(2022, 17) {
    override fun partOne(): Any {
        return calculateHeightOfTowerAfterRocks(inputString, 2022)
    }

    override fun partTwo(): Any {
        return calculateHeightWithPattern(inputString, 1000000000000L)
    }

    fun calculateHeightWithPattern(input: String, numberOfRocks: Long): Long {
        val gasjets = GasJets(input)
        val rocks = Rocks()
        val gasJetsIterator = gasjets.iterator()
        val rocksIterator = rocks.iterator()
        val rockStates = mutableMapOf<RockState, Pair<Long,Long>>()

        // create the chamber with floor
        val chamber = Chamber(7, 60)

        var highest = 0L

        var i = 0L
        var neededRocks = numberOfRocks
        var cycleRockState: RockState? = null
        var heightAtEndOfCycle = 0L
        var indexAtEndOfCycle = 0L
        while (i < neededRocks) {
            val rock = rocksIterator.next()
            var rockLowerLeft = Point2dl(2L, highest + 4)

            var rockIsMoving = true

            while(rockIsMoving) {
                val jet = gasJetsIterator.next()
                var canBePushed = true
                if (jet == '>') {
                    if (rock.width + rockLowerLeft.x < chamber.width) {
                        loop@ for (y in 0 until rock.height) {
                            for (x in rock.width - 1 downTo 0) {
                                if ('#' == rock[Point2d(x, y)]) {
                                    if ('#' == chamber[Point2dl(rockLowerLeft.x + (x + 1), rockLowerLeft.y + y)]) {
                                        canBePushed = false
                                        break@loop
                                    }
                                    break
                                }
                            }
                        }
                        if (canBePushed) {
                            rockLowerLeft = rockLowerLeft.copy(x = rockLowerLeft.x + 1)
                        }
                    }
                } else {
                    if (rockLowerLeft.x > 0) {
                        loop@ for (y in 0 until rock.height) {
                            for (x in 0 until rock.width) {
                                if ('#' == rock[Point2d(x, y)]) {
                                    if ('#' == chamber[Point2dl(rockLowerLeft.x + (x - 1), rockLowerLeft.y + y)]) {
                                        canBePushed = false
                                        break@loop
                                    }
                                    break
                                }
                            }
                        }
                        if (canBePushed) {
                            rockLowerLeft = rockLowerLeft.copy(x = rockLowerLeft.x - 1)
                        }
                    }
                }

                for (x in 0 until rock.width) {
                    for (y in 0 until rock.height) {
                        if ('#' == rock[Point2d(x, y)]) {
                            if (' ' != chamber[Point2dl(rockLowerLeft.x + x, rockLowerLeft.y + y - 1)]) {
                                rockIsMoving = false
                                break
                            }
                        }
                    }

                    if (!rockIsMoving) {
                        break
                    }
                }

                if (rockIsMoving) {
                    rockLowerLeft = rockLowerLeft.copy(y = rockLowerLeft.y - 1)
                }
            }

            for (x in 0 until rock.width) {
                for (y in 0 until rock.height) {
                    if ('#' == rock[Point2d(x, y)]) {
                        chamber[Point2dl(rockLowerLeft.x + x, rockLowerLeft.y + y)] = '#'
                        highest = max(rockLowerLeft.y + y, highest)
                    }
                }
            }

            var y = highest
            val profile = Array(7) { Int.MAX_VALUE }
            while (y > highest - 60 && y >= 0 && profile.any { it == Int.MAX_VALUE }) {
                for (x in profile.mapIndexed { index, i -> if (i == Int.MAX_VALUE) index else -1 }.filter { it != -1 }) {
                    if (chamber[Point2dl(x.toLong(), y)] == '#') {
                        profile[x] = (y - highest).toInt()
                    }
                }
                y--
            }

            i++

            val state = RockState(rocks.current, gasjets.current, profile.toList())
            if (rockStates.containsKey(state) && cycleRockState == null) {
                cycleRockState = state
                val (_, indexOfPrevious) = rockStates[state]!!
                val remainingRocks = numberOfRocks - indexOfPrevious
                val repeats = (remainingRocks) / (i - indexOfPrevious)
                neededRocks = i + (remainingRocks) - (repeats * (i - indexOfPrevious))
                heightAtEndOfCycle = highest
                indexAtEndOfCycle = i
            } else {
                rockStates[state] = Pair(highest, i)
            }
        }

        val (heightOfPrevious, indexOfPrevious) = rockStates[cycleRockState]!!
        val remainingRocks = numberOfRocks - indexOfPrevious
        val repeats = (remainingRocks) / (indexAtEndOfCycle - indexOfPrevious)

        return heightOfPrevious + (repeats * (heightAtEndOfCycle - heightOfPrevious)) + (highest - heightAtEndOfCycle)
    }

    fun calculateHeightOfTowerAfterRocks(input: String, numberOfRocks: Int): Int {
        val gasjets = sequence {
            var current = 0
            while (true) {
                if (current !in input.indices) {
                    current = 0
                }
                yield(input[current++])
            }
        }.iterator()

        // create the chamber with floor
        val chamber = Chamber(7, 50)

        var highest = 0
        val rocks = Rocks().iterator()
        for (i in 0 until numberOfRocks) {
            val rock = rocks.next()
            var rockLowerLeft = Point2d(2, highest + 4)

            var rockIsMoving = true

            while(rockIsMoving) {
                val jet = gasjets.next()
                var canBePushed = true
                if (jet == '>') {
                    if (rock.width + rockLowerLeft.x < chamber.width) {
                        loop@ for (y in 0 until rock.height) {
                            for (x in rock.width - 1 downTo 0) {
                                if ('#' == rock[Point2d(x, y)]) {
                                    if ('#' == chamber[Point2dl((rockLowerLeft.x + (x + 1)).toLong(),
                                            (rockLowerLeft.y + y).toLong()
                                        )]) {
                                        canBePushed = false
                                        break@loop
                                    }
                                    break
                                }
                            }
                        }
                        if (canBePushed) {
                            rockLowerLeft = rockLowerLeft.copy(x = rockLowerLeft.x + 1)
                        }
                    }
                } else {
                    if (rockLowerLeft.x > 0) {
                        loop@ for (y in 0 until rock.height) {
                            for (x in 0 until rock.width) {
                                if ('#' == rock[Point2d(x, y)]) {
                                    if ('#' == chamber[Point2dl((rockLowerLeft.x + (x - 1)).toLong(),
                                            (rockLowerLeft.y + y).toLong()
                                        )]) {
                                        canBePushed = false
                                        break@loop
                                    }
                                    break
                                }
                            }
                        }
                        if (canBePushed) {
                            rockLowerLeft = rockLowerLeft.copy(x = rockLowerLeft.x - 1)
                        }
                    }
                }

                for (x in 0 until rock.width) {
                    for (y in 0 until rock.height) {
                        if ('#' == rock[Point2d(x, y)]) {
                            if (' ' != chamber[Point2dl((rockLowerLeft.x + x).toLong(),
                                    (rockLowerLeft.y + y - 1).toLong()
                                )]) {
                                rockIsMoving = false
                                break
                            }
                        }
                    }

                    if (!rockIsMoving) {
                        break
                    }
                }

                if (rockIsMoving) {
                    rockLowerLeft = rockLowerLeft.copy(y = rockLowerLeft.y - 1)
                }
            }

            for (x in 0 until rock.width) {
                for (y in 0 until rock.height) {
                    if ('#' == rock[Point2d(x, y)]) {
                        chamber[Point2dl((rockLowerLeft.x + x).toLong(),
                            (rockLowerLeft.y + y).toLong()
                        )] = '#'
                        highest = max(rockLowerLeft.y + y, highest)
                    }
                }
            }
        }

        return highest
    }

    class GasJets(private val input: String) {
        var current = -1
        fun iterator(): Iterator<Char> = sequence {
            while (true) {
                current++
                if (current !in input.indices) {
                    current = 0
                }
                yield(input[current])
            }
        }.iterator()
    }

    data class RockState(val rockIndex: Int, val jetIndex: Int, val rockProfile: List<Int>)

    class Rocks {
        var current = -1
        fun iterator() = sequence {
            val rocks = listOf(
                Array2d(4, 1, '#'),
                Array2d(3, 3) { x, y -> if (x % 2 == 0 && y % 2 == 0) ' ' else '#' },
                Array2d(3, 3) { x, y -> if (x == 2 || y == 0) '#' else ' ' },
                Array2d(1, 4, '#'),
                Array2d(2, 2, '#'),
            )

            while (true) {
                current++
                if (current > rocks.lastIndex) {
                    current = 0
                }
                yield(rocks[current])
            }
        }.iterator()
    }

    class Chamber(val width: Int, val height: Int) {
        private var floor = 0L
            set(value) {
                val delta = (value - field).toInt()
                for (y in height - delta until height) {
                    storage[field + y] = Array(width) { ' ' }
                }
                for (y in field until value) {
                    storage.remove(y)
                }
                field = value
            }
        private val storage = mutableMapOf<Long, Array<Char>>()
        init {
            for (y in 0L until height) {
                storage[y] = Array(width) { if (y == 0L) '#' else ' ' }
            }
        }

        operator fun get(point: Point2dl): Char? {
            if (point.y > height + floor) {
                floor = (point.y - height)
            }
            return storage[point.y]?.get(point.x.toInt()) ?: ' '
        }

        operator fun set(point: Point2dl, value: Char) {
            if (point.y > height + floor) {
                floor = (point.y - height)
            }
            storage[point.y]?.set(point.x.toInt(), value)
        }
    }
}