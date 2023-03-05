package fr.sdecout.handson.ordering.domain

import arrow.core.left
import arrow.core.right
import fr.sdecout.handson.ordering.domain.TestMenuItem.Companion.testMenu
import fr.sdecout.handson.ordering.domain.TestMenuItem.LATTE
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import fr.sdecout.handson.ordering.domain.stock.Stock
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty

class OrderingServiceTest {

    private val drinksSentToPreparation = mutableListOf<Drink>()

    @Test
    fun should_process_order() {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock()
        )
        val customer = Customer("Vincent")
        val orderedQuantity = pieces(1)

        val invoice = service.process(Order(LATTE.drink, orderedQuantity, customer))

        expectThat(invoice).isRight(Invoice.from(InvoiceLine(LATTE.drink, orderedQuantity, LATTE.unitPrice)))
        expectThat(drinksSentToPreparation).containsExactly(
            Drink(LATTE.drink, LATTE.recipe, customer)
        )
    }

    @Test
    fun should_process_order_with_more_than_1_drink() {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock()
        )
        val customer = Customer("Vincent")
        val orderedQuantity = pieces(3)

        val invoice = service.process(Order(LATTE.drink, orderedQuantity, customer))

        expectThat(invoice).isRight(Invoice.from(InvoiceLine(LATTE.drink, orderedQuantity, LATTE.unitPrice)))
        expectThat(drinksSentToPreparation).containsExactly(
            Drink(LATTE.drink, LATTE.recipe, customer),
            Drink(LATTE.drink, LATTE.recipe, customer),
            Drink(LATTE.drink, LATTE.recipe, customer)
        )
    }

    @Test
    fun should_fail_to_process_order_if_ingredients_are_missing() {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreOutOfStock()
        )

        val invoice = service.process(Order(LATTE.drink, pieces(1), Customer("Vincent")))

        expectThat(invoice).isLeft(OrderError.UnavailableIngredient(LATTE.recipe.asMap().keys.first()))
        expectThat(drinksSentToPreparation).isEmpty()
    }

    @Test
    fun should_fail_to_process_order_if_stock_is_not_accessible() {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = stockIsNotAccessible()
        )

        val invoice = service.process(Order(LATTE.drink, pieces(1), Customer("Vincent")))

        expectThat(invoice).isLeft(OrderError.UnavailableIngredient(LATTE.recipe.asMap().keys.first()))
        expectThat(drinksSentToPreparation).isEmpty()
    }

    @Test
    fun should_fail_to_process_order_if_drink_is_not_in_menu() {
        val service = OrderingService(
            preparation = { drinksSentToPreparation += it },
            menu = testMenu(),
            stock = allIngredientsAreInStock()
        )
        val drink = DrinkName("UNKNOWN")

        val invoice = service.process(Order(drink, pieces(1), Customer("Vincent")))

        expectThat(invoice).isLeft(OrderError.UnknownDrink(drink))
        expectThat(drinksSentToPreparation).isEmpty()
    }

    private fun allIngredientsAreInStock() = Stock { _, _ -> true.right() }
    private fun allIngredientsAreOutOfStock() = Stock { _, _ -> false.right() }
    private fun stockIsNotAccessible() = Stock { _, _ -> OrderError.StockAccessFailure("Broken").left() }

}