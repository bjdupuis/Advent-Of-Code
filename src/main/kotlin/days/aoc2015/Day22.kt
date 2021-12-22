package days.aoc2015

import days.Day

class Day22 : Day(2015, 22) {
    override fun partOne(): Any {
        return calculateMinimumWinningMana(50, 500, inputList)
    }

    override fun partTwo(): Any {
        return 0
    }

    fun calculateMinimumWinningMana(wizardHitPoints: Int, wizardMana: Int, inputLines: List<String>): Int {
        val bossStartingHitPoints = Regex("Hit Points: (\\d+)").matchEntire(inputLines[0].trim())?.groupValues?.get(1)?.toInt() ?: throw IllegalStateException()
        val bossDamage = Regex("Damage: (\\d+)").matchEntire(inputLines[1].trim())?.groupValues?.get(1)?.toInt() ?: throw IllegalStateException()

        val boss = Boss(bossStartingHitPoints, bossDamage)
        val wizard = Wizard(wizardHitPoints, wizardMana, mutableListOf())

        return processGame(wizard, boss, 0, listOf()).minOf { it }
    }

    private fun processGame(wizard: Wizard, boss: Boss, totalMana: Int, winningManaAmounts: List<Int>): List<Int> {
        var newList = mutableListOf<Int>()
        newList.addAll(winningManaAmounts)
        while (wizard.hitPoints > 0 && boss.hitPoints > 0 && wizard.manaPoints > 0) {
            println("-- Player turn --")
            println("- Player has ${wizard.hitPoints} hit points, ${wizard.armor} armor, ${wizard.manaPoints} mana")
            println("- Boss has ${boss.hitPoints} hit points")

            wizard.spellsInProgress.forEach { spell ->
                spell.performEffect(boss)
            }

            wizard.getCastableSpells().forEach { spell ->
                spell.cast(boss)

                println()
                println("-- Boss turn --")
                println("- Player has ${wizard.hitPoints} hit points, ${wizard.armor} armor, ${wizard.manaPoints} mana")
                println("- Boss has ${boss.hitPoints} hit points")
                val spellsToCast = mutableListOf<Wizard.LastingSpell>()
                spellsToCast.addAll(wizard.spellsInProgress)
                spellsToCast.forEach { spellToCast ->
                    spellToCast.performEffect(boss)
                }
                if (boss.hitPoints > 0) {
                    boss.attack(wizard)
                }

                println()
                val spellsInProgress = mutableListOf<Wizard.LastingSpell>()
                spellsInProgress.addAll(wizard.spellsInProgress)
                newList.addAll(processGame(Wizard(wizard.hitPoints, wizard.manaPoints, spellsInProgress), Boss(boss.hitPoints, boss.damagePerRound), totalMana + spell.cost, winningManaAmounts))
            }
        }

        if (boss.hitPoints <= 0) {
            println("Boss has died! Mana spent is $totalMana")
            return newList + totalMana
        } else if (wizard.hitPoints <= 0){
            println("Player has died :(")
        } else {
            println("Player ran out of mana :(")
        }

        return newList
    }

    class Boss(var hitPoints: Int, val damagePerRound: Int) {
        fun attack(wizard: Wizard) {
            val damage = maxOf((damagePerRound - wizard.armor), 1)
            wizard.hitPoints -= damage
            println("Boss attacks for $damage damage.")
        }
    }

    class Wizard(var hitPoints: Int, var manaPoints: Int, val spellsInProgress: MutableList<LastingSpell>) {
        private val spells = listOf(MagicMissile(), Drain(), Shield(), Poison(), Recharge())
        var armor = 0

        fun getCastableSpells(): List<Spell> {
            return spells.filter { spell ->
                spell.cost <= manaPoints &&
                        (!spellsInProgress.contains(spell) ||
                                (spellsInProgress.contains(spell) && spellsInProgress.first { it == spell }.remainingEffectLength == 1))}
        }

        open inner class Spell(val cost: Int) {
            open fun cast(boss: Boss) {
                manaPoints -= cost
            }
        }

        abstract inner class LastingSpell(cost: Int, private val effectLength: Int) : Spell(cost) {
            var remainingEffectLength = effectLength

            open fun performEffect(boss: Boss) {
                if (--remainingEffectLength == 0) {
                    spellsInProgress.remove(this)
                    println("${javaClass.simpleName} wears off.")
                }
            }

            override fun cast(boss: Boss) {
                super.cast(boss)
                spellsInProgress.add(this)
                performEffect(boss)
            }
        }

        inner class MagicMissile : Spell(53) {
            override fun cast(boss: Boss) {
                super.cast(boss)
                boss.hitPoints -= 4
                println("Player casts Magic Missile, dealing 4 damage.")
            }
        }

        inner class Drain : Spell(73) {
            override fun cast(boss: Boss) {
                super.cast(boss)
                boss.hitPoints -= 2
                hitPoints += 2
                println("Player casts Drain, dealing 2 damage, and healing 2 hit points.")
            }
        }

        inner class Shield: LastingSpell(113, 6) {
            override fun cast(boss: Boss) {
                super.cast(boss)
                armor += 7
                println("Shield's timer is now $remainingEffectLength.")
            }
        }

        inner class Poison : LastingSpell(173, 6) {
            override fun performEffect(boss: Boss) {
                super.performEffect(boss)
                boss.hitPoints -= 3
                println("Poison deals 3 damage; its timer is now $remainingEffectLength.")
            }
        }

        inner class Recharge : LastingSpell(229, 5) {
            override fun performEffect(boss: Boss) {
                super.performEffect(boss)
                manaPoints += 101
                println("Recharge provides 101 mana; its timer is now ${remainingEffectLength}.")
            }
        }
    }
}