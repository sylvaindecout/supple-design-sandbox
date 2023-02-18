package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.centiliters
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.grams
import org.junit.jupiter.api.Test
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class RecipeTest {

    @Test
    fun should_fail_to_initialize_from_empty_list_of_entries() {
        val entries = emptyMap<Ingredient, Quantity>()

        expectThrows<IllegalArgumentException> { Recipe(entries) }
            .message.isEqualTo("Recipe must not be empty")
    }

    @Test
    fun should_fail_to_initialize_from_entries_including_0_quantity() {
        val entries = mapOf(
            Ingredient("Coffee beans") to grams(5),
            Ingredient("Milk") to centiliters(0)
        )

        expectThrows<IllegalArgumentException> { Recipe(entries) }
            .message.isEqualTo("Quantities in a recipe must not be 0")
    }

}