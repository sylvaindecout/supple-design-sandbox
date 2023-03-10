package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class InvoiceLineTest {

    @Test
    fun should_compute_total_price() {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(3),
            unitPrice = Money.of(EUR, 7.00),
        )

        expectThat(invoiceLine.totalPrice)
            .isEqualTo(Money.of(EUR, 21.00))
    }

    @Test
    fun should_compute_total_price_with_0_quantity() {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(0),
            unitPrice = Money.of(EUR, 7.00),
        )

        expectThat(invoiceLine.totalPrice)
            .isEqualTo(Money.zero(EUR))
    }

}
