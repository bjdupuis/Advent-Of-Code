package days.aoc2021

import days.Day

class Day4 : Day(2021, 4) {
    override fun partOne(): Any {
        return parseInput(inputList).calculateFirstWinner()
    }

    override fun partTwo(): Any {
        return parseInput(inputList).calculateLastWinner()
    }

    fun parseInput(input: List<String>): BingoGame {
        val numbers = input.first().split(',').map { it.toInt() }
        val boards = mutableListOf<BingoBoard>()

        input.drop(1).filter { it.isNotBlank() }.chunked(5).let { boardLines ->
            boardLines.forEach {
                boards.add(BingoBoard(it))
            }
        }

        return BingoGame(numbers, boards)
    }

    class BingoGame(private val numbers: List<Int>, private val boards: List<BingoBoard>) {
        fun calculateFirstWinner(): Int {
            numbers.forEach { number ->
                boards.forEach { board ->
                    board.playNumber(number)
                    if (board.isWinner()) {
                        return number * board.calculateWinningScore()
                    }
                }
            }

            throw Exception("No winner found")
        }

        fun calculateLastWinner(): Int {
            var winners = 0
            numbers.forEach { number ->
                boards.forEach { board ->
                    if (!board.isWinner()) {
                        board.playNumber(number)
                        if (board.isWinner()) {
                            if (++winners == boards.size) {
                                return number * board.calculateWinningScore()
                            }
                        }
                    }
                }
            }

            throw Exception("No winner found")
        }
    }

    class BingoBoard(input: List<String>) {
        private var board : Array<Array<Pair<Int,Boolean>>>

        init {
            val list = mutableListOf<Array<Pair<Int,Boolean>>>()
            input.forEach { s ->
                s.trim().split("\\s+".toRegex()).take(5).let { list ->
                    list.map {
                        it.toInt()
                    }
                }.map {
                    Pair(it, false)
                }.toTypedArray().let {
                    list.add(it)
                }
            }

            board = list.toTypedArray()
        }

        fun playNumber(number: Int): Boolean {
            board.forEach { row ->
                for (i in row.indices) {
                    if (row[i].first == number) {
                        row[i] = Pair(row[i].first, true)
                        return true
                    }
                }
            }

            return false
        }

        fun isWinner(): Boolean {
            var winner: Boolean
            for (i in board.indices) {
                val row = board[i]
                winner = true
                for (j in row.indices) {
                    if (!board[i][j].second) {
                        winner = false
                        break
                    }
                }
                if (winner) {
                    return true
                }
            }

            for (i in board[0].indices) {
                winner = true
                for (j in board.indices) {
                    if (!board[j][i].second) {
                        winner = false
                        break
                    }
                }
                if (winner) {
                    return true
                }
            }
            return false
        }

        fun calculateWinningScore(): Int {
            var sum = 0
            board.forEach { row ->
                row.filter { !it.second }.sumBy { it.first }.let { sum += it }
            }

            return sum
        }
    }
}