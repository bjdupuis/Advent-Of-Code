package days.aoc2020

import days.Day

class Day22: Day(2020, 22) {
    override fun partOne(): Any {
        val players = parsePlayers(inputList)
        while (players.none { it.hasLost() }) {
            val drawn = players.mapIndexed { index, player ->
                Pair(index,player.drawCard()!!)
            }

            players[drawn.maxByOrNull { it.second }?.first!!].acceptWonHand(drawn.sortedByDescending { it.second }.map { it.second })
        }

        return players.filterNot { it.hasLost() }.first().calculateWinningHand()
    }

    override fun partTwo(): Any {
        val players = parsePlayers(inputList)

        while (players.none { it.hasLost() }) {
            playGame(players)
        }

        return players.filterNot { it.hasLost() }.first().calculateWinningHand()
    }

    // play a game, return the winner
    private fun playGame(players: List<Player>): Player {
        val previousDeckHashes = mutableSetOf<Int>()

        while (players.none { it.hasLost() }) {
            if (previousDeckHashes.contains(players.hashCode())) {
                return players[0]
            }
            previousDeckHashes.add(players.hashCode())

            val drawn = players.map { player ->
                Pair(player.id, player.drawCard()!!)
            }

            if ((drawn[0].second > players[0].deckSize()) || (drawn[1].second > players[1].deckSize())) {
                players.first { player -> player.id == (drawn.maxByOrNull { it.second }!!.first)}.acceptWonHand(drawn.sortedByDescending { it.second }.map { it.second })
            } else {
                val winner = playGame(listOf(
                        Player(players[0].id, mutableListOf<Int>().apply { addAll(players[0].deck.subList(0,drawn[0].second)) }),
                        Player(players[1].id, mutableListOf<Int>().apply { addAll(players[1].deck.subList(0,drawn[1].second)) })
                ))
                players.first { it.id == winner.id }.acceptWonHand(listOf(
                        drawn.first { it.first == winner.id }.second,
                        drawn.first { it.first != winner.id }.second
                ))
            }
        }

        return players.first { !it.hasLost() }
    }


    private fun parsePlayers(list: List<String>): List<Player> {
        val players = mutableListOf<Player>()
        val deck = mutableListOf<Int>()
        var playerId = 0
        list.forEach {
            when {
                it.isBlank() -> {
                    if (deck.isNotEmpty()) {
                        players.add(Player(playerId, deck))
                    }
                    deck.clear()
                }
                it.contains(":") -> {
                    Regex("Player (\\d):").matchEntire(it)?.destructured?.let { (id) -> playerId = id.toInt() }
                }
                else -> {
                    deck.add(it.toInt())
                }
            }
        }

        return players
    }
}

class Player(val id: Int, initialDeck: List<Int>) {
    val deck = ArrayDeque<Int>()

    init {
        deck.addAll(initialDeck)
    }

    fun deckSize(): Int {
        return deck.size
    }

    fun hasLost(): Boolean {
        return deck.isEmpty()
    }

    fun drawCard(): Int? {
        return deck.removeFirst()
    }

    fun acceptWonHand(cards: List<Int>) {
        deck.addAll(cards)
    }

    fun calculateWinningHand(): Int {
        var index = 1
        var result = 0
        while (deck.isNotEmpty()) {
            result += deck.removeLast() * index++
        }

        return result
    }

    override fun hashCode(): Int {
        return deck.hashCode()
    }
}