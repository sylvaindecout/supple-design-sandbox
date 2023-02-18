package fr.sdecout.handson.ordering.domain

import fr.sdecout.handson.ordering.domain.recipe.Ingredient

sealed class OrderError(val message: String) {

    data class UnknownDrink(private val drink: DrinkName) :
        OrderError("No drink exists with name $drink")

    data class UnavailableIngredient(private val ingredient: Ingredient) :
        OrderError("Ingredient $ingredient is currently unavailable")

    data class StockAccessFailure(private val cause: String) :
        OrderError("Stock access failed - $cause")

}
