package fr.sdecout.handson.ordering.domain

import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class OrderErrorTest {

    @Test
    fun should_initialize_error_for_unknown_drink() {
        val drink = DrinkName("Martini")

        val error = OrderError.UnknownDrink(drink)

        expectThat(error.message).isEqualTo("No drink exists with name $drink")
    }

    @Test
    fun should_initialize_error_for_unavailable_ingredient() {
        val ingredient = Ingredient("Gin")

        val error = OrderError.UnavailableIngredient(ingredient)

        expectThat(error.message).isEqualTo("Ingredient $ingredient is currently unavailable")
    }

    @Test
    fun should_initialize_error_for_stock_access_failure() {
        val cause = "The network is broken"

        val error = OrderError.StockAccessFailure(cause)

        expectThat(error.message).isEqualTo("Stock access failed - $cause")
    }

}