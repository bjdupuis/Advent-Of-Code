package days.aoc2015

import days.Day

// Bob would lose 31 happiness units by sitting next to David.
class Day13: Day(2015, 13) {
    override fun partOne(): Any {
        val people = LinkedHashSet<Person>()
        val edges = LinkedHashSet<PreferenceEdge>()

        inputList.map {
            Regex("(\\w+) would (\\w+) (\\d+) happiness units by sitting next to (\\w+).").matchEntire(it)?.destructured?.let { (subject, disposition, amount, target) ->
                val subjectPerson = people.find { it.name == subject } ?: Person(subject)
                val targetPerson = people.find { it.name == target } ?: Person(target)

                people.add(subjectPerson)
                people.add(targetPerson)

                edges.add(PreferenceEdge(subjectPerson, targetPerson, if (disposition == "lose") amount.toInt().unaryMinus() else amount.toInt()))
            }
        }

        return permute(people.toList()).map { listOfPeople ->
            listOfPeople.mapIndexed { index, person ->
                val left = when (index) {
                    0 -> listOfPeople.last()
                    else -> listOfPeople[index - 1]
                }
                val right = when (index) {
                    listOfPeople.size - 1 -> listOfPeople.first()
                    else -> listOfPeople[index + 1]
                }
                ((edges.find { it.from == person && it.to == left }?.amount ?: 0) + (edges.find { it.from == person && it.to == right }?.amount ?: 0))
            }.sum()
        }.maxOrNull()!!
    }

    fun permute(people: List<Person>): List<List<Person>> {
        if (people.size == 1) {
            return listOf(people)
        }

        val permutations = mutableListOf<List<Person>>()
        val toInsert = people[0]
        for (perm in permute(people.drop(1))) {
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, toInsert)
                permutations.add(newPerm)
            }
        }
        return permutations
    }

    override fun partTwo(): Any {
        val people = LinkedHashSet<Person>()
        val edges = LinkedHashSet<PreferenceEdge>()

        inputList.map {
            Regex("(\\w+) would (\\w+) (\\d+) happiness units by sitting next to (\\w+).").matchEntire(it)?.destructured?.let { (subject, disposition, amount, target) ->
                val subjectPerson = people.find { it.name == subject } ?: Person(subject)
                val targetPerson = people.find { it.name == target } ?: Person(target)

                people.add(subjectPerson)
                people.add(targetPerson)

                edges.add(PreferenceEdge(subjectPerson, targetPerson, if (disposition == "lose") amount.toInt().unaryMinus() else amount.toInt()))
            }
        }

        val me = Person("Brian")
        people.forEach { person ->
            edges.add(PreferenceEdge(me, person, 0))
            edges.add(PreferenceEdge(person, me, 0))
        }
        people.add(me)


        return permute(people.toList()).map { listOfPeople ->
            listOfPeople.mapIndexed { index, person ->
                val left = when (index) {
                    0 -> listOfPeople.last()
                    else -> listOfPeople[index - 1]
                }
                val right = when (index) {
                    listOfPeople.size - 1 -> listOfPeople.first()
                    else -> listOfPeople[index + 1]
                }
                ((edges.find { it.from == person && it.to == left }?.amount ?: 0) + (edges.find { it.from == person && it.to == right }?.amount ?: 0))
            }.sum()
        }.maxOrNull()!!
    }

    class Person(val name: String)

    class PreferenceEdge(val from: Person, val to: Person, val amount: Int)
}