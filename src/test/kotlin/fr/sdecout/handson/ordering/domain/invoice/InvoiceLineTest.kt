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

    @Test
    fun should_add_to_an_invoice() {
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
        val formerInvoice = Invoice.from(line1)

        val updatedInvoice = line2.addTo(formerInvoice)

        expectThat(updatedInvoice)
            .isEqualTo(Invoice.from(line1, line2))
    }

}
