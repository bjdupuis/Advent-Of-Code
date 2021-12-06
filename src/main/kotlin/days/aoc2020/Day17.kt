package days.aoc2020

import days.Day

class Day17: Day(2020, 17) {
    override fun partOne(): Any {
        var cube = Cube(inputList)

        for (i in 0 until 6) {
            cube.calculateNextState()
        }

        return cube.getActiveCount()
    }

    override fun partTwo(): Any {
        var cube = Hypercube(inputList)

        for (i in 0 until 6) {
            cube.calculateNextState()
        }

        return cube.getActiveCount()
    }

    class Cube(input: List<String>) {
        private var sideLength: Int = 0
        private val cube = mutableMapOf<Triple<Int,Int,Int>,Char>()

        init {
            sideLength = input.size
            input.forEachIndexed { yIndex, s ->
                s.forEachIndexed { xIndex, c ->
                    cube[Triple(xIndex - (sideLength / 2), yIndex - (sideLength / 2), 0)] = c
                }
            }
        }

        private fun neighbors(point: Triple<Int,Int,Int>) = sequence {
            for (x in point.first-1..point.first+1)
                for (y in point.second-1..point.second+1)
                    for (z in point.third-1..point.third+1)
                        if (!(point.first == x && point.second == y && point.third == z)) {
                            yield(Triple(x, y, z))
                        }
        }

        fun getActiveCount(): Int {
            return cube.values.count { it == '#'}
        }

        fun calculateNextState() {
            // expand it
            sideLength += 2

            val currentCube = mutableMapOf<Triple<Int,Int,Int>,Char>()
            currentCube.putAll(cube)

            for (x in (sideLength / 2).unaryMinus()..(sideLength / 2))
                for (y in (sideLength / 2).unaryMinus()..(sideLength / 2))
                    for (z in (sideLength / 2).unaryMinus()..(sideLength / 2)) {
                        val point = Triple(x,y,z)
                        val activeNeighbors = neighbors(point).map {
                            currentCube[it]
                        }.count { it == '#' }

                        if ((currentCube[point] ?: '.') == '#') {
                            if(activeNeighbors !in 2..3) {
                                cube[point] = '.'
                            } else {
                                cube[point] = '#'
                            }
                        } else if ((currentCube[point] ?: '.') == '.' && activeNeighbors == 3) {
                            cube[point] = '#'
                        } else {
                            cube[point] = '.'
                        }
                    }
        }

    }

    class Hypercube(input: List<String>) {
        private var sideLength: Int = 0
        private val cube = mutableMapOf<Quad,Char>()

        init {
            sideLength = input.size
            input.forEachIndexed { yIndex, s ->
                s.forEachIndexed { xIndex, c ->
                    cube[Quad(xIndex - (sideLength / 2), yIndex - (sideLength / 2), 0, 0)] = c
                }
            }
        }

        private fun neighbors(point: Quad) = sequence {
            for (x in point.first-1..point.first+1)
                for (y in point.second-1..point.second+1)
                    for (z in point.third-1..point.third+1)
                        for (w in point.fourth-1..point.fourth+1)
                            if (!(point.first == x && point.second == y && point.third == z && point.fourth == w)) {
                                yield(Quad(x, y, z, w))
                            }
        }

        fun getActiveCount(): Int {
            return cube.values.count { it == '#'}
        }

        fun calculateNextState() {
            // expand it
            sideLength += 2

            val currentCube = mutableMapOf<Quad,Char>()
            currentCube.putAll(cube)

            for (x in (sideLength / 2).unaryMinus()..(sideLength / 2))
                for (y in (sideLength / 2).unaryMinus()..(sideLength / 2))
                    for (z in (sideLength / 2).unaryMinus()..(sideLength / 2))
                        for (w in (sideLength / 2).unaryMinus()..(sideLength / 2)) {
                            val point = Quad(x,y,z,w)
                            val activeNeighbors = neighbors(point).map {
                                currentCube[it]
                            }.count { it == '#' }

                            if ((currentCube[point] ?: '.') == '#') {
                                if(activeNeighbors !in 2..3) {
                                    cube[point] = '.'
                                } else {
                                    cube[point] = '#'
                                }
                            } else if ((currentCube[point] ?: '.') == '.' && activeNeighbors == 3) {
                                cube[point] = '#'
                            } else {
                                cube[point] = '.'
                            }
                        }
        }
    }
}

data class Quad(val first: Int, val second:Int, val third:Int, val fourth: Int)
