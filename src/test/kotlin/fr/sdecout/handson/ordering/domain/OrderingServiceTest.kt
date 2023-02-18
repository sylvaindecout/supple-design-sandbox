package fr.sdecout.handson.ordering.domain

import arrow.core.left
import arrow.core.right
import fr.sdecout.handson.ordering.domain.TestMenuItem.Companion.testMenu
import fr.sdecout.handson.ordering.domain.TestMenuItem.LATTE
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import fr.sdecout.handson.ordering.domain.stock.Stock
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly

private val drinksSentToPreparation = mutableListOf<Order>()
private val invoices = mutableListOf<Invoice>()

private val allIngredientsAreInStock = Stock { _, _ -> true.right() }
private val allIngredientsAreOutOfStock = Stock { _, _ -> false.right() }
private val stockIsNotAccessible = Stock { _, _ -> OrderError.StockAccessFailure("Broken").left() }

class OrderingServiceTest : ShouldSpec({

    beforeEach { drinksSentToPreparation.clear() }
    beforeEach { invoices.clear() }

    should("process order") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock,
            invoices = { invoices.add(it) }
        )
        val customer = Customer("Vincent")
        val orderedQuantity = 1.pieces()

        val invoice = service.orchestrateCallsToOtherServicesFor(Order(LATTE.drink, orderedQuantity, customer))

        val expectedInvoice = Invoice.from(InvoiceLine(LATTE.drink, orderedQuantity, LATTE.unitPrice))
        invoice shouldBeRight expectedInvoice
        invoices.shouldContainExactly(expectedInvoice)
        drinksSentToPreparation.shouldContainExactly(
            Order(LATTE.drink, orderedQuantity, customer, LATTE.recipe)
        )
    }

    should("process order with more than 1 drink") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock,
            invoices = { invoices.add(it) }
        )
        val customer = Customer("Vincent")
        val orderedQuantity = 3.pieces()

        val invoice = service.orchestrateCallsToOtherServicesFor(Order(LATTE.drink, orderedQuantity, customer))

        val expectedInvoice = Invoice.from(InvoiceLine(LATTE.drink, orderedQuantity, LATTE.unitPrice))
        invoice shouldBeRight expectedInvoice
        invoices.shouldContainExactly(expectedInvoice)
        drinksSentToPreparation.shouldContainExactly(
            Order(LATTE.drink, orderedQuantity, customer, LATTE.recipe),
            Order(LATTE.drink, orderedQuantity, customer, LATTE.recipe),
            Order(LATTE.drink, orderedQuantity, customer, LATTE.recipe)
        )
    }

    should("fail to process order if ingredients are missing") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreOutOfStock,
            invoices = { invoices.add(it) }
        )

        val invoice = service.orchestrateCallsToOtherServicesFor(Order(LATTE.drink, 1.pieces(), Customer("Vincent")))

        invoice shouldBeLeft OrderError.UnavailableIngredient(LATTE.recipe.asMap().keys.first())
        invoices.shouldBeEmpty()
        drinksSentToPreparation.shouldBeEmpty()
    }

    should("fail to process order if stock is not accessible") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = stockIsNotAccessible,
            invoices = { invoices.add(it) }
        )

        val invoice = service.orchestrateCallsToOtherServicesFor(Order(LATTE.drink, 1.pieces(), Customer("Vincent")))

        invoice shouldBeLeft OrderError.UnavailableIngredient(LATTE.recipe.asMap().keys.first())
        invoices.shouldBeEmpty()
        drinksSentToPreparation.shouldBeEmpty()
    }

    should("fail to process order if drink is not in menu") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock,
            invoices = { invoices.add(it) }
        )
        val drink = DrinkName("UNKNOWN")

        val invoice = service.orchestrateCallsToOtherServicesFor(Order(drink, 1.pieces(), Customer("Vincent")))

        invoice shouldBeLeft OrderError.UnknownDrink(drink)
        invoices.shouldBeEmpty()
        drinksSentToPreparation.shouldBeEmpty()
    }

})
