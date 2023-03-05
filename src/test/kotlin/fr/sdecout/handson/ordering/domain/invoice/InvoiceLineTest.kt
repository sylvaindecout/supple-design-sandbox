package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money

class InvoiceLineTest : ShouldSpec({

    should("compute total price") {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 3.pieces(),
            unitPrice = Money.of(EUR, 7.00),
        )

        invoiceLine.totalPrice shouldBe Money.of(EUR, 21.00)
    }

    should("compute total price with 0 quantity") {
        val invoiceLine = InvoiceLine(
            drink = DrinkName("ESPRESSO"),
            quantity = 0.pieces(),
            unitPrice = Money.of(EUR, 7.00),
        )

        invoiceLine.totalPrice shouldBe Money.zero(EUR)
    }

})
