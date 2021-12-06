package days.aoc2020

import days.Day

class Day21: Day(2020, 21) {
    override fun partOne(): Any {
        var potentialIngredientsForAllergen = mutableMapOf<String, MutableSet<String>>()
        val allIngredients = mutableMapOf<String, Int>()
        inputList.forEach {
            Regex("(.*) \\(contains (.*)\\)").matchEntire(it)?.destructured?.let { (ingredients, allergens) ->
                val potentialIngredients = ingredients.split(" ")
                potentialIngredients.forEach { ingredient ->
                    allIngredients[ingredient] = allIngredients.getOrDefault(ingredient, 0) + 1
                }
                allergens.split(", ").forEach { allergen ->
                    if (!potentialIngredientsForAllergen.containsKey(allergen)) {
                        potentialIngredientsForAllergen[allergen] = mutableSetOf()
                        potentialIngredientsForAllergen[allergen]!!.addAll(potentialIngredients)
                    }
                    potentialIngredientsForAllergen[allergen] = potentialIngredientsForAllergen[allergen]?.intersect(potentialIngredients)?.toMutableSet() ?: mutableSetOf()
                }
            }
        }

        return allIngredients.filter { ingredient ->
            potentialIngredientsForAllergen.values.none { it.contains(ingredient.key) }
        }.map {
            it.value
        }.sum()
    }

    override fun partTwo(): Any {
        var potentialIngredientsForAllergen = mutableMapOf<String, MutableSet<String>>()
        val allIngredients = mutableMapOf<String, Int>()
        inputList.forEach {
            Regex("(.*) \\(contains (.*)\\)").matchEntire(it)?.destructured?.let { (ingredients, allergens) ->
                val potentialIngredients = ingredients.split(" ")
                potentialIngredients.forEach { ingredient ->
                    allIngredients[ingredient] = allIngredients.getOrDefault(ingredient, 0) + 1
                }
                allergens.split(", ").forEach { allergen ->
                    if (!potentialIngredientsForAllergen.containsKey(allergen)) {
                        potentialIngredientsForAllergen[allergen] = mutableSetOf()
                        potentialIngredientsForAllergen[allergen]!!.addAll(potentialIngredients)
                    }
                    potentialIngredientsForAllergen[allergen] = potentialIngredientsForAllergen[allergen]?.intersect(potentialIngredients)?.toMutableSet() ?: mutableSetOf()
                }
            }
        }

        do {
            val knownBadIngredients = potentialIngredientsForAllergen.filter { ingredientMap ->
                ingredientMap.value.size == 1
            }.map { it.value.first() }.toSet()

            potentialIngredientsForAllergen.filter { it.value.size > 1 && it.value.intersect(knownBadIngredients).isNotEmpty() }.forEach { candidate ->
                candidate.value.removeIf { knownBadIngredients.contains(it) }
            }

        } while(potentialIngredientsForAllergen.count { it.value.size > 1 } > 0)

        return potentialIngredientsForAllergen.toSortedMap().map {
            it.value.first()
        }.joinToString(",")
    }
}