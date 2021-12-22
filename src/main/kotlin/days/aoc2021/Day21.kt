package days.aoc2021

import days.Day

class Day21 : Day(2021, 21) {
    override fun partOne(): Any {
        return calculateScoresForGame(inputList)
    }

    override fun partTwo(): Any {
        return calculateDiracWinners(inputList)
    }

    fun calculateDiracWinners(inputList: List<String>): Long {
        var player1Position = inputList.first().last() - '0'
        var player2Position = inputList.last().last() - '0'

        val (player1Wins, player2Wins) = playDiracGame(player1Position, 0, player2Position, 0)
        return maxOf(player1Wins, player2Wins)
    }

    fun calculateScoresForGame(inputList: List<String>): Long {
        var player1Position = inputList.first().last() - '0'
        var player2Position = inputList.last().last() - '0'
        var player1Score = 0L
        var player2Score = 0L
        val determinateDice = DeterminateDice(100,3)


        while (player1Score < 1000 && player2Score < 1000) {
            var roll = determinateDice.roll()
            player1Position = (((player1Position - 1) + roll) % 10) + 1
            player1Score += player1Position
            if (player1Score >= 1000) {
                break
            }

            roll = determinateDice.roll()
            player2Position = (((player2Position - 1) + roll) % 10) + 1
            player2Score += player2Position
            if (player1Score >= 1000) {
                break
            }
        }

        return minOf(player1Score, player2Score) * determinateDice.totalRolls

    }

    class DeterminateDice(private val size: Int, private val rolls: Int) {
        private var current = 1
        var totalRolls = 0

        fun roll(): Int {

            var value = 0
            repeat(rolls) {
                value += current++
                if (current > size) {
                    current = 1
                }
            }
            totalRolls += rolls
            return value
        }
    }

    data class GameState(val position: Int, val score: Int, val otherPosition: Int, val otherScore: Int)

    private val cache = mutableMapOf<GameState,Pair<Long,Long>>()
    private fun playDiracGame(position: Int, score: Int, otherPosition: Int, otherScore: Int): Pair<Long,Long> {
        val gameState = GameState(position, score, otherPosition, otherScore)

        return cache.computeIfAbsent(gameState) {
            when {
                score >= 21 -> {
                    Pair(1L, 0L)
                }
                otherScore >= 21 -> {
                    Pair(0L, 1L)
                }
                else -> {
                    var winners = Pair(0L, 0L)
                    for (i in 1..3)
                        for (j in 1..3)
                            for (k in 1..3) {
                                val newPosition = (((position - 1) + (i + j + k)) % 10) + 1
                                val newScore = score + newPosition
                                val (otherWinners, currentWinners) = playDiracGame(
                                    otherPosition,
                                    otherScore,
                                    newPosition,
                                    newScore
                                )
                                winners =
                                    Pair(winners.first + currentWinners, winners.second + otherWinners)
                            }

                    winners
                }
            }
        }
    }
}