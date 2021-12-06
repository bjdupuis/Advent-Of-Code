package days.aoc2015

import days.Day
import java.lang.Integer.max

class Day6: Day(2015, 6) {
    override fun partOne(): Any {
        val lights: Array<BooleanArray> = Array(1000) { BooleanArray(1000) }
        inputList.forEach { line ->
            when {
                line.startsWith("turn on") -> {
                    Regex("turn on (\\d+),(\\d+) through (\\d+),(\\d+)")
                            .matchEntire(line)
                            ?.destructured
                            ?.let { (startX, startY, endX, endY) ->
                        for (x in startX.toInt()..endX.toInt()) {
                            for (y in startY.toInt()..endY.toInt()) {
                                lights[x][y] = true
                            }
                        }
                    }
                }
                line.startsWith("turn off") -> {
                    Regex("turn off (\\d+),(\\d+) through (\\d+),(\\d+)")
                            .matchEntire(line)
                            ?.destructured
                            ?.let { (startX, startY, endX, endY) ->
                                for (x in startX.toInt()..endX.toInt()) {
                                    for (y in startY.toInt()..endY.toInt()) {
                                        lights[x][y] = false
                                    }
                                }
                            }
                }
                line.startsWith("toggle") -> {
                    Regex("toggle (\\d+),(\\d+) through (\\d+),(\\d+)")
                            .matchEntire(line)
                            ?.destructured
                            ?.let { (startX, startY, endX, endY) ->
                                for (x in startX.toInt()..endX.toInt()) {
                                    for (y in startY.toInt()..endY.toInt()) {
                                        lights[x][y] = !lights[x][y]
                                    }
                                }
                            }

                }
            }
        }

        var count = 0
        for (x in lights.indices) {
            val lightColumns = lights[x]
            for (y in lightColumns.indices) {
                if (lights[x][y]) {
                    count++
                }
            }
        }

        return count
    }


    override fun partTwo(): Any {
        val lights: Array<IntArray> = Array(1000) { IntArray(1000) { 0 } }
        inputList.forEach { line ->
            when {
                line.startsWith("turn on") -> {
                    Regex("turn on (\\d+),(\\d+) through (\\d+),(\\d+)")
                            .matchEntire(line)
                            ?.destructured
                            ?.let { (startX, startY, endX, endY) ->
                                for (x in startX.toInt()..endX.toInt()) {
                                    for (y in startY.toInt()..endY.toInt()) {
                                        lights[x][y] = lights[x][y] + 1
                                    }
                                }
                            }
                }
                line.startsWith("turn off") -> {
                    Regex("turn off (\\d+),(\\d+) through (\\d+),(\\d+)")
                            .matchEntire(line)
                            ?.destructured
                            ?.let { (startX, startY, endX, endY) ->
                                for (x in startX.toInt()..endX.toInt()) {
                                    for (y in startY.toInt()..endY.toInt()) {
                                        lights[x][y] = max(lights[x][y] - 1, 0)
                                    }
                                }
                            }
                }
                line.startsWith("toggle") -> {
                    Regex("toggle (\\d+),(\\d+) through (\\d+),(\\d+)")
                            .matchEntire(line)
                            ?.destructured
                            ?.let { (startX, startY, endX, endY) ->
                                for (x in startX.toInt()..endX.toInt()) {
                                    for (y in startY.toInt()..endY.toInt()) {
                                        lights[x][y] = lights[x][y] + 2
                                    }
                                }
                            }

                }
            }
        }

        var count = 0L
        for (x in lights.indices) {
            val lightColumns = lights[x]
            for (y in lightColumns.indices) {
                if (lights[x][y] > 0) {
                    count += lights[x][y]
                }
            }
        }

        return count
    }
}