package days.aoc2015

import days.Day

class Day15: Day(2015, 15) {
    override fun partOne(): Any {
        val ingredients = inputList.map(::parseIngredients)

        val recipes = permuteRecipes(100, ingredients, null)

        return recipes.map { recipe ->
            recipe.contents.flatMap { (amount, ingredient) ->
                ingredient.attributes.mapNotNull { (propertyWeight, property) ->
                    if (property != "calories") Pair(propertyWeight * amount, property) else null
                }
            }
        }.map { propertyList ->
            val tmp = propertyList.groupBy {
                it.second
            }.map {
                it.value.sumBy { propertyTotal ->
                    propertyTotal.first
                }
            }.map {
                if (it < 0) 0 else it
            }
            tmp.reduce { acc, i -> acc * i }
        }.maxOrNull()!!

    }

    private fun permuteRecipes(capacityLeft: Int, ingredients: List<Ingredient>, currentRecipeSoFar: Recipe?): List<Recipe> {
        val recipes = mutableListOf<Recipe>()
        if (ingredients.size == 1) {
            if (capacityLeft > 0) {
                currentRecipeSoFar?.contents?.add(Pair(capacityLeft, ingredients[0]))
            }
            recipes.add(currentRecipeSoFar!!)
        } else {
            val ingredient = ingredients[0]

            for (i in capacityLeft downTo 0) {
                val recipe = Recipe(currentRecipeSoFar)
                if (i > 0) {
                    recipe.contents.add(Pair(i, ingredient))
                }
                recipes.addAll(permuteRecipes(capacityLeft - i, ingredients.drop(1), recipe))
            }
        }

        return recipes
    }

    private tailrec fun sequenceRecipes(capacityLeft: Int, ingredients: List<Ingredient>, currentRecipeSoFar: Recipe?) = sequence {
        if (ingredients.size == 1) {
            if (capacityLeft > 0) {
                currentRecipeSoFar?.contents?.add(Pair(capacityLeft, ingredients[0]))
            }
            yield(currentRecipeSoFar!!)
        } else {
            val ingredient = ingredients[0]

            for (i in capacityLeft downTo 0) {
                val recipe = Recipe(currentRecipeSoFar)
                if (i > 0) {
                    recipe.contents.add(Pair(i, ingredient))
                }
                permuteRecipes(capacityLeft - i, ingredients.drop(1), recipe)
            }
        }
    }

    override fun partTwo(): Any {
        val ingredients = inputList.map(::parseIngredients)

        val recipes = permuteRecipes(100, ingredients, null)

        return recipes.map { recipe ->
            recipe.contents.flatMap { (amount, ingredient) ->
                ingredient.attributes.mapNotNull { (propertyWeight, property) ->
                    Pair(propertyWeight * amount, property)
                }
            }
        }.map { propertyList ->
            val tmp = propertyList.groupBy {
                it.second
            }.map {
                Pair(it.key, it.value.sumBy { propertyTotal ->
                    propertyTotal.first
                })
            }.map {
                if (it.first == "calories") {
                    if (it.second == 500) 1 else 0
                } else {
                    it.second
                }
            }.map {
                if (it < 0) 0 else it
            }
            tmp.reduce { acc, i -> acc * i }
        }.maxOrNull()!!
    }

}

fun parseIngredients(string: String): Ingredient {
    return Ingredient(string.substring(string.indexOf(':') + 2).split(", ").map {
        Regex("(\\w+) (-)?(\\d+)").matchEntire(it)?.destructured?.let { (attribute, sign, amount) ->
            Pair(if ("-" == sign) amount.toInt().unaryMinus() else amount.toInt(), attribute)
        }!!
    })
}

class Ingredient(val attributes: List<Pair<Int, String>>)

class Recipe {
    constructor(currentRecipeSoFar: Recipe?) {
        contents.addAll(currentRecipeSoFar?.contents ?: listOf())
    }

    val contents = mutableListOf<Pair<Int,Ingredient>>()
}
