package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money

class InvoiceTest : ShouldSpec({

    should("compute total price") {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 3.pieces(),
            unitPrice = Money.of(EUR, 7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = 1.pieces(),
            unitPrice = Money.of(EUR, 12.00),
        )
        val invoice = Invoice.from(line1, line2)

        invoice.totalPrice shouldBe Money.of(EUR, 33.00)
    }

    should("compute total price with 0 lines") {
        val invoice = Invoice.from()

        invoice.totalPrice shouldBe Money.zero(EUR)
    }

    should("add a line") {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 3.pieces(),
            unitPrice = Money.of(EUR, 7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = 1.pieces(),
            unitPrice = Money.of(EUR, 12.00),
        )
        val formerInvoice = Invoice.from(line1)

        val updatedInvoice = formerInvoice + line2

        updatedInvoice shouldBe Invoice.from(line1, line2)
    }

})