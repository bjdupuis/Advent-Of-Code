package days.aoc2022

import days.Day
import kotlin.math.max

class Day19 : Day(2022, 19) {
    override fun partOne(): Any {
        return determineSumOfBlueprintQualityLevels(inputList, 24)
    }

    override fun partTwo(): Any {
        return 0
    }

    fun determineSumOfBlueprintQualityLevels(input: List<String>, minutes: Int): Int {
        return input.map { parseBlueprint(it) }.sumOf { BlueprintOperation(it, minutes).getQualityOfBlueprint() }
    }

    // Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
    private fun parseBlueprint(line: String): Blueprint {
        Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
            .matchEntire(line)?.destructured?.let { (id, oreRobotOreCost, clayRobotOreCost, obsidianRobotOreCost, obsidianRobotClayCost, geodeRobotOreCost, geodeRobotObsidianCost) ->
                return Blueprint(id.toInt(), oreRobotOreCost.toInt(), clayRobotOreCost.toInt(), obsidianRobotOreCost.toInt(), obsidianRobotClayCost.toInt(), geodeRobotOreCost.toInt(), geodeRobotObsidianCost.toInt())
            }

        throw IllegalStateException()
    }
}

class BlueprintOperation(private val blueprint: Blueprint, private val minutes: Int) {
    private val robotFactories = listOf(
        OreRobotFactory(blueprint.oreRobotOreCost),
        ClayRobotFactory(blueprint.clayRobotOreCost),
        ObsidianRobotFactory(blueprint.obsidianRobotOreCost, blueprint.obsidianRobotClayCost),
        GeodeRobotFactory(blueprint.geodeRobotOreCost, blueprint.geodeRobotObsidianCost),
    )

    fun getQualityOfBlueprint(): Int {
        return blueprint.id * findMostGeodes(minutes)
    }

    private fun findMostGeodes(minutes: Int): Int {
        val queue: MutableList<OperationState> = mutableListOf(OperationState(
            minutes,
            mutableMapOf(Resource.Ore to 1, Resource.Clay to 0, Resource.Obsidian to 0, Resource.Geode to 0),
            mutableMapOf(Resource.Ore to 0, Resource.Clay to 0, Resource.Obsidian to 0, Resource.Geode to 0),
            mutableListOf(),
        ))
        var maxGeodes = 0
        val seen = mutableSetOf<OperationState>()
        while (queue.isNotEmpty()) {
            with(queue.removeFirst()) {
                if (this !in seen) {
                    seen.add(this)
                    val totalResources = resources.entries.associate { entry ->
                        entry.key to entry.value + robots.getOrDefault(entry.key, 0)
                    }

                    val forwardRobots = robots.entries.associate { entry ->
                        entry.key to entry.value + activeFactories.count { it == entry.key }
                    }
                    if (minutesLeft > 0) {
                        robotFactories.filter { it.canAfford(totalResources) }.forEach {
                            queue.add(OperationState(
                                minutesLeft - 1,
                                forwardRobots,
                                totalResources.entries.associate { entry ->
                                    entry.key to entry.value - it.cost().getOrDefault(entry.key, 0)
                                },
                                listOf(it.resource())
                            ))
                        }

                        queue.add(
                            OperationState(
                                minutesLeft - 1,
                                forwardRobots,
                                totalResources,
                                emptyList()
                            )
                        )
                    } else {
                        maxGeodes = max(maxGeodes, totalResources.getOrDefault(Resource.Geode, 0))
                    }
                }
            }
        }

        return maxGeodes
    }
}

data class OperationState(val minutesLeft: Int, val robots: Map<Resource, Int>, val resources: Map<Resource, Int>, val activeFactories: List<Resource>)

data class Blueprint(val id: Int,
                val oreRobotOreCost: Int,
                val clayRobotOreCost: Int,
                val obsidianRobotOreCost: Int,
                val obsidianRobotClayCost: Int,
                val geodeRobotOreCost: Int,
                val geodeRobotObsidianCost: Int,
)

abstract class RobotFactory {
    abstract fun canAfford(resources: Map<Resource, Int>): Boolean

    abstract fun resource(): Resource

    abstract fun cost(): Map<Resource, Int>
}

class OreRobotFactory(private val oreCost: Int): RobotFactory() {
    override fun canAfford(resources: Map<Resource, Int>): Boolean {
        return resources.getOrDefault(Resource.Ore, 0) >= oreCost
    }

    override fun resource(): Resource = Resource.Ore

    override fun cost(): Map<Resource, Int> {
        return mapOf(Resource.Ore to oreCost)
    }
}

class ClayRobotFactory(private val oreCost: Int): RobotFactory() {
    override fun canAfford(resources: Map<Resource, Int>): Boolean {
        return resources.getOrDefault(Resource.Ore, 0) >= oreCost
    }

    override fun resource(): Resource = Resource.Clay

    override fun cost(): Map<Resource, Int> {
        return mapOf(Resource.Ore to oreCost)
    }
}

class ObsidianRobotFactory(private val oreCost: Int, private val clayCost: Int): RobotFactory() {
    override fun canAfford(resources: Map<Resource, Int>): Boolean {
        return resources.getOrDefault(Resource.Ore, 0) >= oreCost &&
                resources.getOrDefault(Resource.Clay, 0) >= clayCost
    }

    override fun resource(): Resource = Resource.Obsidian

    override fun cost(): Map<Resource, Int> {
        return mapOf(
            Resource.Ore to oreCost,
            Resource.Clay to clayCost
        )
    }
}

class GeodeRobotFactory(private val oreCost: Int, private val obsidianCost: Int): RobotFactory() {
    override fun canAfford(resources: Map<Resource, Int>): Boolean {
        return resources.getOrDefault(Resource.Ore, 0) >= oreCost &&
                resources.getOrDefault(Resource.Obsidian, 0) >= obsidianCost
    }

    override fun resource(): Resource = Resource.Geode

    override fun cost(): Map<Resource, Int> {
        return mapOf(
            Resource.Ore to oreCost,
            Resource.Obsidian to obsidianCost
        )
    }
}

enum class Resource {
    Ore, Clay, Obsidian, Geode
}
