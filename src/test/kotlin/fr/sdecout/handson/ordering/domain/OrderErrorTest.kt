package fr.sdecout.handson.ordering.domain

import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class OrderErrorTest : ShouldSpec({

    should("initialize error for unknown drink") {
        val drink = DrinkName("Martini")

        val error = OrderError.UnknownDrink(drink)

        error.message shouldBe "No drink exists with name $drink"
    }

    should("initialize error for unavailable ingredient") {
        val ingredient = Ingredient("Gin")

        val error = OrderError.UnavailableIngredient(ingredient)

        error.message shouldBe "Ingredient $ingredient is currently unavailable"
    }

    should("initialize error for stock access failure") {
        val cause = "The network is broken"

        val error = OrderError.StockAccessFailure(cause)

        error.message shouldBe "Stock access failed - $cause"
    }

})