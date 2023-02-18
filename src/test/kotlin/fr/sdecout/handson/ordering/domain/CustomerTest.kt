package fr.sdecout.handson.ordering.domain

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class CustomerTest {

    @Test
    fun should_fail_to_initialize_from_blank_name() {
        expectThrows<IllegalArgumentException> { Customer("   ") }
            .message.isEqualTo("Customer name must not be blank")
    }

    @Test
    fun should_display_as_string() {
        expectThat(Customer("Vincent").toString()).isEqualTo("Vincent")
    }

}
