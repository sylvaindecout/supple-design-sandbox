package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Money
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class InvoiceLineTest : ShouldSpec({

    should("compute total price") {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 3.pieces(),
            unitPrice = Money(7.00),
        )

        invoiceLine.totalPrice shouldBe Money(21.00)
    }

     should("compute total price with 0 quantity") {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 0.pieces(),
            unitPrice = Money(7.00),
        )

        invoiceLine.totalPrice shouldBe Money(0.0)
    }

     should("add to an invoice") {
        val line1 = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 3.pieces(),
            unitPrice = Money(7.00),
        )
        val line2 = InvoiceLine(
            drink = DrinkName("LATTE"),
            quantity = 1.pieces(),
            unitPrice = Money(12.00),
        )
        val formerInvoice = Invoice.from(line1)

        val updatedInvoice = line2.addTo(formerInvoice)

        updatedInvoice shouldBe Invoice.from(line1, line2)
    }

})
