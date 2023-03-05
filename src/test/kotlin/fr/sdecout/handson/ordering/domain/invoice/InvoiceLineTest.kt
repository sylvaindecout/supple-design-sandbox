package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Money
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class InvoiceLineTest {

    @Test
    fun should_compute_total_price() {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(3),
            unitPrice = Money(7.00),
        )

        expectThat(invoiceLine.totalPrice)
            .isEqualTo(Money(21.00))
    }

    @Test
    fun should_compute_total_price_with_0_quantity() {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(0),
            unitPrice = Money(7.00),
        )

        expectThat(invoiceLine.totalPrice)
            .isEqualTo(Money(0.0))
    }

}
