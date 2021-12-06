package days.aoc2020

import days.Day

class Day24: Day(2020, 24) {
    override fun partOne(): Any {
        return partOneSolution(inputList)
    }

    fun calculateStartingGrid(list: List<String>) : HexGrid<HexTile> {
        val grid = HexGrid<HexTile>()

        var currentCoordinate = Triple(0,0,0)
        list.forEach { directions ->
            var i = 0
            while (i in directions.indices) {
                when (directions[i]) {
                    'n' -> {
                        i++
                        when (directions[i]) {
                            'e' -> {
                                currentCoordinate = grid.coordinateTo(currentCoordinate, HexGrid.Direction.NORTHEAST)
                            }
                            'w' -> {
                                currentCoordinate = grid.coordinateTo(currentCoordinate, HexGrid.Direction.NORTHWEST)
                            }
                        }
                    }
                    's' -> {
                        i++
                        when (directions[i]) {
                            'e' -> {
                                currentCoordinate = grid.coordinateTo(currentCoordinate, HexGrid.Direction.SOUTHEAST)
                            }
                            'w' -> {
                                currentCoordinate = grid.coordinateTo(currentCoordinate, HexGrid.Direction.SOUTHWEST)
                            }
                        }
                    }
                    'e' -> {
                        currentCoordinate = grid.coordinateTo(currentCoordinate, HexGrid.Direction.EAST)
                    }
                    'w' -> {
                        currentCoordinate = grid.coordinateTo(currentCoordinate, HexGrid.Direction.WEST)
                    }
                }
                i++
            }

            grid.getOrInsertDefault(currentCoordinate, HexTile()).flip()
            currentCoordinate = Triple(0,0,0)
        }
        return grid
    }

    fun partOneSolution(list: List<String>): Int {
        return calculateStartingGrid(list).map.count { it.value.color == HexTile.Color.BLACK}
    }

    override fun partTwo(): Any {
        val grid = calculateStartingGrid(inputList)

        println("There are ${grid.map.count { it.value.color == HexTile.Color.BLACK }} tiles initially")

        for (i in 1..100) {
            val currentMap = mutableMapOf<Triple<Int, Int, Int>, HexTile>()
            grid.map.forEach { (triple, hexTile) ->
                currentMap.putIfAbsent(triple, HexTile())
                if (hexTile.color == HexTile.Color.BLACK) {
                    currentMap[triple]!!.flip()
                }
            }
            grid.map.keys.forEach { coordinate ->
                HexGrid.Direction.values().forEach { direction ->
                    currentMap.getOrPut(grid.coordinateTo(coordinate, direction)) { HexTile() }
                }
            }

            currentMap.forEach { entry ->
                val countOfBlackNeighboringTiles = HexGrid.Direction.values().count { direction ->
                    currentMap.getOrDefault(grid.coordinateTo(entry.key, direction), HexTile()).color == HexTile.Color.BLACK
                }
                when {
                    entry.value.color == HexTile.Color.BLACK && (countOfBlackNeighboringTiles == 0 || countOfBlackNeighboringTiles > 2) -> {
                        val actualTile = grid.getOrInsertDefault(entry.key, HexTile())
                        if (actualTile.color == HexTile.Color.BLACK) {
                            actualTile.flip()
                        }
                    }
                    entry.value.color == HexTile.Color.WHITE && countOfBlackNeighboringTiles == 2 -> {
                        val actualTile = grid.getOrInsertDefault(entry.key, HexTile())
                        if (actualTile.color == HexTile.Color.WHITE) {
                            actualTile.flip()
                        }
                    }
                    else -> {
                        // do nothing
                    }

                }
            }

            println("After $i day(s) there are ${grid.map.count { it.value.color == HexTile.Color.BLACK }} tiles")
        }

        return grid.map.count { it.value.color == HexTile.Color.BLACK}
    }
}

class HexTile {

    var color = Color.WHITE

    fun flip() {
        color = if (color == Color.WHITE) Color.BLACK else Color.WHITE
    }

    enum class Color {
        WHITE, BLACK
    }
}

class HexGrid<T> {
    val map = mutableMapOf<Triple<Int,Int,Int>, T>()

    fun getOrInsertDefault(coordinate: Triple<Int,Int,Int>, default: T): T {
        return if (map.containsKey(coordinate)) {
            map[coordinate]!!
        } else {
            map[coordinate] = default
            map[coordinate]!!
        }
    }

    fun set(coordinate: Triple<Int,Int,Int>, value: T) {
        map[coordinate] = value
    }

    fun coordinateTo(startingCoordinate: Triple<Int, Int, Int>, direction: Direction): Triple<Int,Int,Int> {
        val delta = direction.delta()
        return Triple(
                startingCoordinate.first + delta.first,
                startingCoordinate.second + delta.second,
                startingCoordinate.third + delta.third
        )
    }

    enum class Direction(val value: String) {
        NORTHEAST("ne"), EAST("e"), SOUTHEAST("se"), SOUTHWEST("sw"), WEST("w"), NORTHWEST("nw");

        fun delta(): Triple<Int,Int,Int> {
            return when(this) {
                NORTHEAST -> Triple(1, 0, -1)
                EAST -> Triple(1, -1, 0)
                SOUTHEAST -> Triple(0, -1, 1)
                SOUTHWEST -> Triple(-1, 0, 1)
                WEST -> Triple(-1, 1, 0)
                NORTHWEST -> Triple(0, 1, -1)
            }
        }
    }
}