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

    override fun process(order: Order) = menu.find(order.drink)
        .flatMap { failOnUnavailableIngredient(it, order.quantity) }
        .onRight { startPreparation(order.withRecipe(it.recipe)) }
        .map { buildInvoice(order.withRecipe(it.recipe), it) }

    private fun failOnUnavailableIngredient(menuItem: MenuItem, orderedQuantity: Quantity.Scalar) = menuItem.recipe
        .asMap().toList()
        .map { (ingredient, requiredUnitQuantity) -> ingredient to requiredUnitQuantity * orderedQuantity }
        .firstOrNull { (ingredient, requiredQuantity) -> !ingredient.isAvailable(requiredQuantity) }
        ?.let { (ingredient, _) -> OrderError.UnavailableIngredient(ingredient).left() }
        ?: menuItem.right()

    private fun Ingredient.isAvailable(requiredQuantity: Quantity) = stock.hasEnoughOf(this, requiredQuantity)
        .getOrElse { false }

    private fun startPreparation(orderWithRecipe: Order) = repeat(orderWithRecipe.quantity.amount) {
        preparation.queueForPreparation(orderWithRecipe)
    }

    // TODO: Side-effect-free functions - Saving the invoice is a side effect, it should be segregated from invoice generation function.
    private fun buildInvoice(orderWithRecipe: Order, menuItem: MenuItem): Invoice {
        return menuItem.toInvoiceWith(orderWithRecipe.quantity)
            .also { this.invoices.add(it) }
    }

    private fun MenuItem.toInvoiceWith(quantity: Quantity.Scalar): Invoice {
        return Invoice.from(InvoiceLine(
            drink = this.name,
            quantity = quantity,
            unitPrice = this.unitPrice
        ))
    }

}

