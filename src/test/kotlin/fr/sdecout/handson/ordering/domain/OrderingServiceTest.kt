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

private val allIngredientsAreInStock = Stock { _, _ -> true.right() }
private val allIngredientsAreOutOfStock = Stock { _, _ -> false.right() }
private val stockIsNotAccessible = Stock { _, _ -> OrderError.StockAccessFailure("Broken").left() }

class OrderingServiceTest : ShouldSpec({

    beforeEach { drinksSentToPreparation.clear() }

    should("process order") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock
        )
        val customer = Customer("Vincent")
        val orderedQuantity = 1.pieces()

        val invoice = service.process(Order(LATTE.drink, orderedQuantity, customer))

        invoice shouldBeRight Invoice.from(InvoiceLine(LATTE.drink, orderedQuantity, LATTE.unitPrice))
        drinksSentToPreparation.shouldContainExactly(
            Order(LATTE.drink, orderedQuantity, customer, LATTE.recipe)
        )
    }

    should("process order with more than 1 drink") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock
        )
        val customer = Customer("Vincent")
        val orderedQuantity = 3.pieces()

        val invoice = service.process(Order(LATTE.drink, orderedQuantity, customer))

        invoice shouldBeRight Invoice.from(InvoiceLine(LATTE.drink, orderedQuantity, LATTE.unitPrice))
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
            stock = allIngredientsAreOutOfStock
        )

        val invoice = service.process(Order(LATTE.drink, 1.pieces(), Customer("Vincent")))

        invoice shouldBeLeft OrderError.UnavailableIngredient(LATTE.recipe.asMap().keys.first())
        drinksSentToPreparation.shouldBeEmpty()
    }

    should("fail to process order if stock is not accessible") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = stockIsNotAccessible
        )

        val invoice = service.process(Order(LATTE.drink, 1.pieces(), Customer("Vincent")))

        invoice shouldBeLeft OrderError.UnavailableIngredient(LATTE.recipe.asMap().keys.first())
        drinksSentToPreparation.shouldBeEmpty()
    }

    should("fail to process order if drink is not in menu") {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock
        )
        val drink = DrinkName("UNKNOWN")

        val invoice = service.process(Order(drink, 1.pieces(), Customer("Vincent")))

        invoice shouldBeLeft OrderError.UnknownDrink(drink)
        drinksSentToPreparation.shouldBeEmpty()
    }

})
