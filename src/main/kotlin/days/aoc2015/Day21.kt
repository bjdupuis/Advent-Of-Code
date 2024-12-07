package days.aoc2015

import days.Day
import util.combinations
import kotlin.math.max
import kotlin.math.min

class Day21 : Day(2015, 21) {

    companion object {
        const val STARTING_HIT_POINTS = 100
    }

    override fun partOne(): Any {
        return calculatePartOne()
    }

    override fun partTwo(): Any {
        return calculatePartTwo()
    }

    fun calculatePartOne(): Int {
        return calculateLowestCostForWinningPlayer(weapons, armor, rings)
    }

    private fun costOfWinningPlayer(player: Player): Int {
        return if (doesPlayerWin(player)) {
            player.totalCost()
        } else {
            Int.MAX_VALUE
        }
    }

    private fun calculateLowestCostForWinningPlayer(weapons: List<Weapon>, armors: List<Armor>, rings: List<ShopItem>): Int {
        return weapons.minOf { weapon ->
            min(
                costOfWinningPlayer(Player(listOf(weapon))),
                armors.minOf { armor ->
                    min(
                        costOfWinningPlayer(Player(listOf(weapon, armor))),
                        rings.combinations(1, 2).minOf { ringsWorn ->
                            min(
                                costOfWinningPlayer(Player(listOf(weapon) + ringsWorn)),
                                costOfWinningPlayer(Player(listOf(weapon, armor) + ringsWorn))
                            )
                        }
                    )
                }
            )
        }
    }

    private fun doesPlayerWin(player: Player): Boolean {
        val boss = Boss()

        while(true) {
            boss.hitPoints -= max(player.damage() - boss.armor(), 1)
            if (boss.hitPoints <= 0) {
                return true
            }
            player.hitPoints -= max(boss.damage() - player.armor(), 1)
            if (player.hitPoints <= 0) {
                return false
            }
        }
    }

    fun calculatePartTwo(): Int {
        return calculateHighestCostForLosingPlayer(weapons, armor, rings)
    }

    private fun costOfLosingPlayer(player: Player): Int {
        return if (!doesPlayerWin(player)) {
            player.totalCost()
        } else {
            Int.MIN_VALUE
        }
    }

    private fun calculateHighestCostForLosingPlayer(weapons: List<Weapon>, armors: List<Armor>, rings: List<ShopItem>): Int {
        return weapons.maxOf { weapon ->
            max(
                costOfLosingPlayer(Player(listOf(weapon))),
                armors.maxOf { armor ->
                    max(
                        costOfLosingPlayer(Player(listOf(weapon, armor))),
                        rings.combinations(1, 2).maxOf { ringsWorn ->
                            max(
                                costOfLosingPlayer(Player(listOf(weapon) + ringsWorn)),
                                costOfLosingPlayer(Player(listOf(weapon, armor) + ringsWorn))
                            )
                        }
                    )
                }
            )
        }
    }

    internal abstract class Participant(startingHitPoints: Int) {
        var hitPoints = startingHitPoints

        abstract fun armor(): Int

        abstract fun damage(): Int
    }

    internal class Player(val items: List<ShopItem>): Participant(STARTING_HIT_POINTS) {

        override fun armor(): Int {
            return items.filterIsInstance<Protector>().sumOf { it.protection }
        }

        override fun damage(): Int {
            return items.filterIsInstance<Damager>().sumOf { it.damage }
        }

        fun totalCost(): Int {
            return items.sumOf { it.cost }
        }

        override fun toString(): String {
            return "Player(items=$items)"
        }


    }

    internal class Boss(): Participant(109) {
        override fun armor(): Int {
            return 2
        }

        override fun damage(): Int {
            return 8
        }
    }

    internal open class ShopItem(val cost: Int)

    internal open class Damager(cost: Int, val damage: Int): ShopItem(cost)

    internal open class Protector(cost: Int, val protection: Int): ShopItem(cost)

    internal class Weapon(cost: Int, damage: Int): Damager(cost, damage) {
        override fun toString(): String {
            return "Weapon($cost, $damage)"
        }
    }

    internal class Armor(cost: Int, protection: Int): Protector(cost, protection) {
        override fun toString(): String {
            return "Armor($cost, $protection)"
        }
    }

    internal class DamagingRing(cost: Int, damage: Int): Damager(cost, damage)

    internal class ProtectingRing(cost: Int, protection: Int): Protector(cost, protection)

    private val weapons = listOf(
        Weapon(8, 4),
        Weapon(10, 5),
        Weapon(25, 6),
        Weapon(40, 7),
        Weapon(74, 8)
    )

    private val armor = listOf(
        Armor(13, 1),
        Armor(31, 2),
        Armor(53, 3),
        Armor(75, 4),
        Armor(102, 5)
    )

    private val rings = listOf(
        DamagingRing(25, 1),
        DamagingRing(50, 2),
        DamagingRing(100, 3),
        ProtectingRing(20, 1),
        ProtectingRing(40, 2),
        ProtectingRing(80, 3)
    )
}