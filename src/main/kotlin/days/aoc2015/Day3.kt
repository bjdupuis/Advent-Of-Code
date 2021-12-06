package days.aoc2015

import days.Day

class Day3: Day(2015, 3) {
    override fun partOne(): Any {
        var santaLocation = Pair(0,0)
        val houseMap = mutableMapOf<Pair<Int,Int>,Int>()
        inputString.forEach {
            houseMap.putIfAbsent(santaLocation, 0)
            houseMap[santaLocation] = houseMap[santaLocation]!! + 1
            santaLocation = newLocationFromDirection(santaLocation, it)!!
        }

        return houseMap.size
    }

    private fun newLocationFromDirection(currentLocation: Pair<Int,Int>, direction: Char): Pair<Int,Int>? {
        return when(direction) {
            'v' -> Pair(currentLocation.first, currentLocation.second + 1)
            '^' -> Pair(currentLocation.first, currentLocation.second - 1)
            '>' -> Pair(currentLocation.first + 1, currentLocation.second)
            '<' -> Pair(currentLocation.first - 1, currentLocation.second)
            else -> null
        }
    }

    override fun partTwo(): Any {
        var santaLocation = Pair(0,0)
        var robotLocation = Pair(0,0)
        var selectedLocation: Pair<Int,Int> = santaLocation
        val houseMap = mutableMapOf<Pair<Int,Int>,Int>()
        inputString.forEach {
            houseMap.putIfAbsent(selectedLocation, 0)
            houseMap[selectedLocation] = houseMap[selectedLocation]!! + 1

            if (selectedLocation == santaLocation) {
                santaLocation = newLocationFromDirection(santaLocation, it)!!
                selectedLocation = robotLocation
            } else {
                robotLocation = newLocationFromDirection(robotLocation, it)!!
                selectedLocation = santaLocation
            }
        }

        return houseMap.size
    }
}