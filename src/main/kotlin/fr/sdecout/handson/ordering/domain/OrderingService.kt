package fr.sdecout.handson.ordering.domain

import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.invoice.Invoices
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import fr.sdecout.handson.ordering.domain.stock.Stock

@DomainDrivenDesign.Service
class OrderingService(
    private val preparation: DrinkPreparation,
    private val menu: Menu,
    private val stock: Stock,
    private val invoices: Invoices
) : CustomerOrderHandler {

    // TODO: Declarative design - Intention-revealing interfaces, side-effect free functions and assertions are a first step into declarative territory, but we can go further: how can we make it so that someone changing the design does not have to care about the 'how', only about the 'what'.
    override fun process(order: Order) = menu.find(order.drink)
        .flatMap { failOnUnavailableIngredient(it, order.quantity) }
        .onRight { startPreparation(it, order) }
        .map { it.toInvoiceWith(order.quantity) }
        .onRight { invoices.add(it) }

    private fun failOnUnavailableIngredient(menuItem: MenuItem, orderedQuantity: Quantity.Scalar) = menuItem.recipe
        .asMap().toList()
        .map { (ingredient, requiredUnitQuantity) -> ingredient to requiredUnitQuantity * orderedQuantity }
        .firstOrNull { (ingredient, requiredQuantity) -> !ingredient.isAvailable(requiredQuantity) }
        ?.let { (ingredient, _) -> OrderError.UnavailableIngredient(ingredient).left() }
        ?: menuItem.right()

    private fun Ingredient.isAvailable(requiredQuantity: Quantity) = stock.hasEnoughOf(this, requiredQuantity)
        .getOrElse { false }

    private fun startPreparation(menuItem: MenuItem, order: Order) = menuItem
        .asDrinkFor(order.customer)
        .let { drink -> repeat(order.quantity.amount) { preparation.queueForPreparation(drink) } }

    private fun MenuItem.asDrinkFor(customer: Customer) = Drink(
        name = this.name,
        recipe = this.recipe,
        customer = customer
    )

    private fun MenuItem.toInvoiceWith(quantity: Quantity.Scalar): Invoice {
        return Invoice.from(InvoiceLine(
            drink = this.name,
            quantity = quantity,
            unitPrice = this.unitPrice
        ))
    }

}

