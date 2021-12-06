package days.aoc2020

import days.Day

class Day7: Day(2020, 7) {
    fun createBagList(list: List<String>): Map<String,Bag> {
        val bags: MutableMap<String,Bag> = mutableMapOf()

        list.forEach {
            Regex("(\\w+ \\w+)").find(it)?.destructured?.let { (bagDescriptor) ->
                bags.putIfAbsent(bagDescriptor, Bag(bagDescriptor))
                val bag = bags[bagDescriptor]!!

                Regex("(\\d+) (\\w+ \\w+) bag").findAll(it)?.forEach {
                    it.destructured?.let { (count, descriptor) ->
                        bags.putIfAbsent(descriptor, Bag(descriptor))
                        bag.descendants.add(Pair(bags[descriptor]!!, count.toInt()))

                        bags[descriptor]!!.ancestors.add(bag)
                    }
                }
            }
        }

        return bags
    }

    fun countDescendents(bag: Bag): Int {
        val result = bag.descendants.map { (containedBag, number) ->
                val descendants = countDescendents(containedBag) + 1
                number * if (descendants > 0) descendants else 1
            }.sum()

        return result
    }

    override fun partTwo(): Any {
        val bags = createBagList(inputList)

        return countDescendents(bags["shiny gold"]!!)
    }

    override fun partOne(): Any {
        val bags = createBagList(inputList)

        return countAncestors(bags["shiny gold"]!!)
    }

    class Bag(val descriptor: String) {
        val descendants: MutableList<Pair<Bag, Int>> = mutableListOf()
        val ancestors: MutableSet<Bag> = mutableSetOf()
    }

    fun countAncestors(bag: Bag): Int {
        return traverseAncestors(bag, mutableSetOf()).count()
    }

    fun traverseAncestors(bag: Bag, bags: MutableSet<Bag>): MutableSet<Bag> {
        bags.addAll(bag.ancestors)
        bag.ancestors.forEach {
            traverseAncestors(it, bags)
        }

        return bags
    }


}