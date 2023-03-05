package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.Either
import arrow.core.getOrElse
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity

class FailOnUnavailableIngredientStep(
    private val unknownAsUnavailable: Boolean,
    private val hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>
) : OrderingDslStep {

    override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = menuItem.recipe
        .asMap().toList()
        .map { (ingredient, requiredUnitQuantity) -> ingredient to requiredUnitQuantity * order.quantity }
        .firstOrNull { (ingredient, requiredQuantity) -> !ingredient.isAvailable(requiredQuantity) }
        ?.let { (ingredient, _) -> Pair(OrderError.UnavailableIngredient(ingredient), invoice) }
        ?: Pair(null, invoice)

    private fun Ingredient.isAvailable(requiredQuantity: Quantity) = hasEnoughStockOf(this, requiredQuantity)
        .getOrElse { !unknownAsUnavailable }

}