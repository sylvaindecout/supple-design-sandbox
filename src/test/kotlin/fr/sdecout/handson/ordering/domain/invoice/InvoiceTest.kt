package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Money
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class InvoiceTest {

    @Test
    fun should_compute_total_price() {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(3),
            unitPrice = Money(7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = pieces(1),
            unitPrice = Money(12.00),
        )
        val invoice = Invoice.from(line1, line2)

        expectThat(invoice.totalPrice)
            .isEqualTo(Money(33.00))
    }

    @Test
    fun should_compute_total_price_with_0_lines() {
        val invoice = Invoice.from()

        expectThat(invoice.totalPrice)
            .isEqualTo(Money(0.0))
    }

}