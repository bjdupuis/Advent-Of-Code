package days.aoc2015

import days.Day

class Day9: Day(2015, 9) {
    override fun partOne(): Any {
        val segments: MutableList<Segment> = mutableListOf()
        val startingPoints: MutableSet<Location> = mutableSetOf()
        val destinations: MutableSet<Location> = mutableSetOf()
        val locations: MutableSet<Location> = mutableSetOf()

        inputList.forEach {
            Regex("(\\w+) to (\\w+) = (\\d+)").matchEntire(it)?.destructured?.let { (start, destination, distance) ->
                startingPoints.add(Location(start))
                destinations.add(Location(destination))
                Segment(startingPoints.first { it.name == start }, destinations.first { it.name == destination }, distance.toInt()).let { segment ->
                    segments.add(segment)
                }
            }
        }

        locations.addAll(startingPoints.plus(destinations))

        return locations.mapIndexedNotNull { outerIndex, startingLocation ->
            locations.filterIndexed { innerIndex, _ -> innerIndex > outerIndex }
                    .mapNotNull {
                        print("Trying ${startingLocation.name} to ${it.name}: ")
                        val result = calculateMinimumLengthToNode(startingLocation, it, segments, mutableListOf(), locations)
                        if (result == null) {
                            println(" invalid")
                        } else {
                            println(" valid [$result]")
                        }
                        result
                    }.minOrNull()
        }.minOrNull()!!

    }

    data class Location(val name: String)

    class Segment(val first: Location, val second: Location, val distance: Int)

    fun calculateMinimumLengthToNode(startingPoint: Location, endingPoint: Location, segments: List<Segment>, visited: List<Location>, toVisit: Set<Location>): Int? {
        if (startingPoint == endingPoint) {
            return if (toVisit.isEmpty()) {
                print("Visited ")
                visited.forEach {
                    print("${it.name} -> " )
                }
                println("${startingPoint.name}")

                println("** valid path found **")
                0
            } else {
                null
            }
        }
        return segments.asSequence().filter { ( it.first == startingPoint || it.second == startingPoint ) }
                .filter { if (it.first == startingPoint) { it.second in toVisit } else { it.first in toVisit } }
                .map {
                    val travelingTo = if (it.first == startingPoint) it.second else it.first
                    val distance = calculateMinimumLengthToNode(travelingTo, endingPoint, segments, visited.plus(startingPoint), toVisit.minus(startingPoint).minus(travelingTo))
                    if (distance == null) {
                        Int.MAX_VALUE
                    } else {
                        distance + it.distance
                    }
                }
                .filter { it != Int.MAX_VALUE }
                .map {
                    println("Distance is $it")
                    it
                }.minOrNull()
    }

    fun calculateMaximumLengthToNode(startingPoint: Location, endingPoint: Location, segments: List<Segment>, visited: List<Location>, toVisit: Set<Location>): Int? {
        if (startingPoint == endingPoint) {
            return if (toVisit.isEmpty()) {
                print("Visited ")
                visited.forEach {
                    print("${it.name} -> " )
                }
                println("${startingPoint.name}")

                println("** valid path found **")
                0
            } else {
                null
            }
        }
        return segments.asSequence().filter { ( it.first == startingPoint || it.second == startingPoint ) }
                .filter { if (it.first == startingPoint) { it.second in toVisit } else { it.first in toVisit } }
                .map {
                    val travelingTo = if (it.first == startingPoint) it.second else it.first
                    val distance = calculateMaximumLengthToNode(travelingTo, endingPoint, segments, visited.plus(startingPoint), toVisit.minus(startingPoint).minus(travelingTo))
                    if (distance == null) {
                        Int.MIN_VALUE
                    } else {
                        distance + it.distance
                    }
                }
                .filter { it != Int.MIN_VALUE }
                .map {
                    println("Distance is $it")
                    it
                }.maxOrNull()
    }

    override fun partTwo(): Any {
        val segments: MutableList<Segment> = mutableListOf()
        val startingPoints: MutableSet<Location> = mutableSetOf()
        val destinations: MutableSet<Location> = mutableSetOf()
        val locations: MutableSet<Location> = mutableSetOf()

        inputList.forEach {
            Regex("(\\w+) to (\\w+) = (\\d+)").matchEntire(it)?.destructured?.let { (start, destination, distance) ->
                startingPoints.add(Location(start))
                destinations.add(Location(destination))
                Segment(startingPoints.first { it.name == start }, destinations.first { it.name == destination }, distance.toInt()).let { segment ->
                    segments.add(segment)
                }
            }
        }

        locations.addAll(startingPoints.plus(destinations))

        return locations.mapIndexedNotNull { outerIndex, startingLocation ->
            locations.filterIndexed { innerIndex, _ -> innerIndex > outerIndex }
                    .mapNotNull {
                        print("Trying ${startingLocation.name} to ${it.name}: ")
                        val result = calculateMaximumLengthToNode(startingLocation, it, segments, mutableListOf(), locations)
                        if (result == null) {
                            println(" invalid")
                        } else {
                            println(" valid [$result]")
                        }
                        result
                    }.maxOrNull()
        }.maxOrNull()!!
    }
}