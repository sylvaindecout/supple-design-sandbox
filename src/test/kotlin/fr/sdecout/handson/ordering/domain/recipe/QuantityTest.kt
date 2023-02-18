package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.error
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import fr.sdecout.handson.ordering.domain.recipe.UnitOfMeasure.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import strikt.assertions.message

class QuantityTest {

    @Test
    fun should_fail_to_initialize_from_negative_amount() {
        expectThrows<IllegalArgumentException> { Quantity.of(-1, GRAMS) }
            .message.isEqualTo("Quantity must not be negative")
    }

    @Test
    fun should_identify_quantity_with_0_amount_as_zero() {
        expectThat(Quantity.of(0, GRAMS).isZero).isTrue()
    }

    @Test
    fun should_not_identify_quantity_with_positive_amount_as_zero() {
        expectThat(Quantity.of(1, GRAMS).isZero).isFalse()
    }

    @Test
    fun should_multiply() {
        expectThat(Quantity.of(3, GRAMS) * pieces(2))
            .isEqualTo(Quantity.of(6, GRAMS))
    }

    @Test
    fun should_fail_to_multiply_error() {
        expectThat((error() * pieces(2)).isError()).isTrue()
    }

    @Test
    fun should_add() {
        expectThat(Quantity.of(3, GRAMS) + Quantity.of(2, GRAMS))
            .isEqualTo(Quantity.of(5, GRAMS))
    }

    @Test
    fun should_fail_to_add_if_result_is_above_max() {
        val max = Quantity.of(Int.MAX_VALUE, MILLIGRAMS)

        val result = max + Quantity.of(1, MILLIGRAMS)

        expectThat(result.isError()).isTrue()
    }

    @Test
    fun should_fail_to_add_if_units_are_inconsistent() {
        val mass = Quantity.of(3, GRAMS)
        val volume = Quantity.of(2, CENTILITERS)

        val result = mass + volume

        expectThat(result.isError()).isTrue()
    }

    @Test
    fun should_fail_to_add_error() {
        expectThat((aValidQuantity() + error()).isError()).isTrue()
    }

    @Test
    fun should_fail_to_add_from_error() {
        expectThat((error() + aValidQuantity()).isError()).isTrue()
    }

    @Test
    fun should_subtract() {
        expectThat(Quantity.of(3, GRAMS) - Quantity.of(2, GRAMS))
            .isEqualTo(Quantity.of(1, GRAMS))
    }

    @Test
    fun should_subtract_greater_value_to_0() {
        expectThat(Quantity.of(3, GRAMS) - Quantity.of(10, GRAMS))
            .isEqualTo(Quantity.of(0, GRAMS))
    }

    @Test
    fun should_fail_to_subtract_if_units_are_inconsistent() {
        val mass = Quantity.of(3, GRAMS)
        val volume = Quantity.of(2, CENTILITERS)

        val result = mass - volume

        expectThat(result.isError()).isTrue()
    }

    @Test
    fun should_fail_to_subtract_error() {
        expectThat((aValidQuantity() - error()).isError()).isTrue()
    }

    @Test
    fun should_fail_to_subtract_from_error() {
        expectThat((error() - aValidQuantity()).isError()).isTrue()
    }

    @Test
    fun should_compare_quantities() {
        expectThat(Quantity.of(3, GRAMS) > Quantity.of(10, GRAMS)).isFalse()
    }

    @Test
    fun should_fail_to_compare_quantities_if_units_are_inconsistent() {
        val mass = Quantity.of(3, GRAMS)
        val volume = Quantity.of(2, CENTILITERS)

        expectThrows<IllegalArgumentException> { mass > volume }
            .message.isEqualTo("Operations do not apply to quantities with inconsistent units of measure ($MILLIGRAMS / $MILLILITERS)")
    }

    @Test
    fun should_fail_to_compare_quantities_if_this_is_an_error() {
        expectThrows<IllegalStateException> { error() > aValidQuantity() }
            .message.isEqualTo("Invalid instance")
    }

    @Test
    fun should_fail_to_compare_quantities_if_other_is_an_error() {
        expectThrows<IllegalStateException> { aValidQuantity() > error() }
            .message.isEqualTo("Invalid instance")
    }

    @Test
    fun should_display_as_string() {
        expectThat(Quantity.of(3, GRAMS).toString()).isEqualTo("3000mg")
    }

    @Test
    fun should_display_as_string_with_no_unit_of_measure() {
        expectThat(Quantity.of(3, null).toString()).isEqualTo("3 pieces")
    }

    @Test
    fun should_display_error_as_string() {
        expectThat(error().toString()).isEqualTo("(error)")
    }

    private fun aValidQuantity() = Quantity.of(3, GRAMS)

}
