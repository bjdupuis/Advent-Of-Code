package days.aoc2021

import days.Day

class Day25 : Day(2021, 25) {
    override fun partOne(): Any {
        return calculateFirstStepWhereCucmbersDontMove(inputList)
    }

    override fun partTwo(): Any {
        return 0
    }

    fun calculateFirstStepWhereCucmbersDontMove(inputList: List<String>): Int {
        val width = inputList.first().length
        val height = inputList.size
        var seafloor = Array(inputList.size) {
            Array(inputList.first().length) {'.'}
        }

        inputList.forEachIndexed { y, line ->
            line.trim().forEachIndexed { x, c -> seafloor[y][x] = c }
        }

        var steps = 0
        var moved = Int.MAX_VALUE

        while(moved != 0) {
            moved = 0
            steps++

            for (y in 0 until height) {
                (0 until width).filter { x ->
                    seafloor[y][x] == '>'
                }.filter { x ->
                    seafloor[y][if (x == width - 1) 0 else x + 1] == '.'
                }.forEach { x ->
                    moved++
                    seafloor[y][x] = '.'
                    seafloor[y][if (x == width - 1) 0 else x + 1] = '>'
                }
            }
            for (x in 0 until width) {
                (0 until height).filter { y ->
                    seafloor[y][x] == 'v'
                }.filter { y ->
                    seafloor[if (y == height - 1) 0 else y + 1][x] == '.'
                }.forEach { y ->
                    moved++
                    seafloor[y][x] = '.'
                    seafloor[if (y == height - 1) 0 else y + 1][x] = 'v'
                }
            }
        }

        return steps
    }

}