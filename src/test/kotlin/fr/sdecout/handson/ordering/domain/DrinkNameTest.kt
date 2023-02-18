package fr.sdecout.handson.ordering.domain

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.*

class DrinkNameTest {

    @Test
    fun should_fail_to_initialize_from_blank_name() {
        expectThrows<IllegalArgumentException> { DrinkName("   ") }
            .message.isEqualTo("Drink name must not be blank")
    }

    @Test
    fun should_display_as_string() {
        expectThat(DrinkName("Latte").toString()).isEqualTo("Latte")
    }

}
