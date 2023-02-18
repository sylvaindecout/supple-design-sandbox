package fr.sdecout.handson.ordering.domain.recipe

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class IngredientTest {

    @Test
    fun should_fail_to_initialize_from_blank_name() {
        expectThrows<IllegalArgumentException> { Ingredient("   ") }
            .message.isEqualTo("Ingredient name must not be blank")
    }

    @Test
    fun should_display_as_string() {
        expectThat(Ingredient("Coffee beans").toString()).isEqualTo("Coffee beans")
    }

}
