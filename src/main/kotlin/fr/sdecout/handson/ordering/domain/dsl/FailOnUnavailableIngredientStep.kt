package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.getOrElse
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import fr.sdecout.handson.ordering.domain.stock.Stock

class FailOnUnavailableIngredientStep(
    private val unknownAsUnavailable: Boolean,
    private val stock: Stock
) : SpecificationStep {

    override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = menuItem.recipe
        .asMap().toList()
        .map { (ingredient, requiredUnitQuantity) -> ingredient to requiredUnitQuantity * order.quantity }
        .firstOrNull { (ingredient, requiredQuantity) -> !ingredient.isAvailable(requiredQuantity) }
        ?.let { (ingredient, _) -> Pair(OrderError.UnavailableIngredient(ingredient), invoice) }
        ?: Pair(null, invoice)

    private fun Ingredient.isAvailable(requiredQuantity: Quantity) = stock.hasEnoughOf(this, requiredQuantity)
        .getOrElse { !unknownAsUnavailable }

}