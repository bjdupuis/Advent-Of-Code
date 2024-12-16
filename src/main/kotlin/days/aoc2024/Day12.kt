package days.aoc2024

import days.Day
import days.aoc2022.Day9
import util.CharArray2d
import util.Point2d

class Day12 : Day(2024, 12) {
    override fun partOne(): Any {
        return calculatePartOne(inputList)
    }

    override fun partTwo(): Any {
        return calculatePartTwo(inputList)
    }

    fun calculatePartOne(input: List<String>): Int {
        val map = CharArray2d(input)
        val visited = mutableSetOf<Point2d>()
        val regions = mutableListOf<RegionSize>()
        map.iterator().forEach {
            if (!visited.contains(it)) {
                regions.add(calculateSizeOfContiguousRegion(map, it, visited))
            }
        }

        return regions.map { it.area * it.perimeter }.sum()
    }

    fun calculateSizeOfContiguousRegion(map: CharArray2d, current: Point2d, visited: MutableSet<Point2d>): RegionSize {
        val regionalNeighbors = current.neighbors().filter { !visited.contains(it) && it.isWithin(map) && map[current] == map[it] }
        return if (visited.add(current)) {
            RegionSize(
                1,
                current.neighbors().count { !it.isWithin(map) || map[current] != map[it] }
            ) + regionalNeighbors.regionSumOf { calculateSizeOfContiguousRegion(map, it, visited) }
        } else {
            RegionSize(0, 0)
        }
    }

    data class RegionSize(val area: Int, val perimeter: Int) {
        operator fun plus(increment: RegionSize): RegionSize {
            return RegionSize(area + increment.area, perimeter + increment.perimeter)
        }
    }

    fun calculatePartTwo(input: List<String>): Int {
        val map = CharArray2d(input)
        val regions = mutableListOf<Pair<Char,Pair<Int,Int>>>()
        val totalVisited = mutableSetOf<Point2d>()
        map.iterator().forEach {
            if (!totalVisited.contains(it)) {
                val currentSet = mutableSetOf<Point2d>()
                val corners = calculateCornersForRegion(map, it, currentSet)
                regions.add(Pair(map[it], Pair(currentSet.size, corners)))
                totalVisited.addAll(currentSet)
            }
        }

        return regions.map { it.second.first * it.second.second }.sum()
    }

    fun calculateCornersForRegion(map: CharArray2d, current: Point2d, visited: MutableSet<Point2d>): Int {
        var corners = 0
        if (visited.contains(current)) {
            return 0
        }
        if ((!current.northernNeighbor.isWithin(map) || (current.northernNeighbor.isWithin(map) && map[current.northernNeighbor] != map[current])) &&
            (!current.westernNeighbor.isWithin(map) || (current.westernNeighbor.isWithin(map) && map[current.westernNeighbor] != map[current]))) {
            corners++
        } else if (current.northernNeighbor.isWithin(map) && map[current.northernNeighbor] == map[current] &&
            current.westernNeighbor.isWithin(map) && map[current.westernNeighbor] == map[current] &&
            map[current.northWesternNeighbor] != map[current]) {
            corners++
        }
        if ((!current.northernNeighbor.isWithin(map) || (current.northernNeighbor.isWithin(map) && map[current.northernNeighbor] != map[current])) &&
            (!current.easternNeighbor.isWithin(map) || (current.easternNeighbor.isWithin(map) && map[current.easternNeighbor] != map[current]))) {
            corners++
        } else if (current.northernNeighbor.isWithin(map) && map[current.northernNeighbor] == map[current] &&
            current.easternNeighbor.isWithin(map) && map[current.easternNeighbor] == map[current] &&
            map[current.northEasternNeighbor] != map[current]) {
            corners++
        }
        if ((!current.southernNeighbor.isWithin(map) || (current.southernNeighbor.isWithin(map) && map[current.southernNeighbor] != map[current])) &&
            (!current.westernNeighbor.isWithin(map) || (current.westernNeighbor.isWithin(map) && map[current.westernNeighbor] != map[current]))) {
            corners++
        } else if (current.southernNeighbor.isWithin(map) && map[current.southernNeighbor] == map[current] &&
            current.westernNeighbor.isWithin(map) && map[current.westernNeighbor] == map[current] &&
            map[current.southWesternNeighbor] != map[current]) {
            corners++
        }
        if ((!current.southernNeighbor.isWithin(map) || (current.southernNeighbor.isWithin(map) && map[current.southernNeighbor] != map[current])) &&
            (!current.easternNeighbor.isWithin(map) || (current.easternNeighbor.isWithin(map) && map[current.easternNeighbor] != map[current]))) {
            corners++
        } else if (current.southernNeighbor.isWithin(map) && map[current.southernNeighbor] == map[current] &&
            current.easternNeighbor.isWithin(map) && map[current.easternNeighbor] == map[current] &&
            map[current.southEasternNeighbor] != map[current]) {
            corners++
        }

        visited.add(current)
        return corners + current.neighbors().filter { !visited.contains(it) && it.isWithin(map) && map[it] == map[current] }.sumOf { point: Point2d -> calculateCornersForRegion(map, point, visited) }
    }

}


private inline fun Iterable<Point2d>.regionSumOf(selector: (Point2d) -> Day12.RegionSize): Day12.RegionSize {
    var sum = Day12.RegionSize(0, 0)
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
