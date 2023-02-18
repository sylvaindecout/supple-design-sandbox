package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.centiliters
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.grams
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class RecipeTest : ShouldSpec({

    should("fail to initialize from empty list of entries") {
        val entries = emptyMap<Ingredient, Quantity>()

        shouldThrow<IllegalArgumentException> { Recipe(entries) }
            .message shouldBe "Recipe must not be empty"
    }

    should("fail to initialize from entries including 0 quantity") {
        val entries = mapOf(
            Ingredient("Coffee beans") to 5.grams(),
            Ingredient("Milk") to 0.centiliters()
        )

        shouldThrow<IllegalArgumentException> { Recipe(entries) }
            .message shouldBe "Quantities in a recipe must not be 0"
    }

})