package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import org.joda.money.CurrencyMismatchException
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.CurrencyUnit.USD
import org.joda.money.Money
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class InvoiceTest {

    @Test
    fun should_compute_total_price() {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(3),
            unitPrice = Money.of(EUR, 7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = pieces(1),
            unitPrice = Money.of(EUR, 12.00),
        )
        val invoice = Invoice.from(line1, line2)

        expectThat(invoice.totalPrice)
            .isEqualTo(Money.of(EUR, 33.00))
    }

    @Test
    fun should_compute_total_price_with_0_lines() {
        val invoice = Invoice.from()

        expectThat(invoice.totalPrice)
            .isEqualTo(Money.zero(EUR))
    }

    @Test
    fun should_fail_to_compute_total_price_if_lines_have_inconsistent_currencies() {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = Quantity.pieces(3),
            unitPrice = Money.of(EUR, 7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = Quantity.pieces(1),
            unitPrice = Money.of(USD, 12.00),
        )
        val invoice = Invoice.from(line1, line2)

        expectThrows<CurrencyMismatchException> { invoice.totalPrice }
            .message.isEqualTo("Currencies differ: $EUR/$USD")
    }

    @Test
    fun should_add_a_line() {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = pieces(3),
            unitPrice = Money.of(EUR, 7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = pieces(1),
            unitPrice = Money.of(EUR,12.00),
        )
        val formerInvoice = Invoice.from(line1)

        val updatedInvoice = formerInvoice + line2

        expectThat(updatedInvoice)
            .isEqualTo(Invoice.from(line1, line2))
    }

}