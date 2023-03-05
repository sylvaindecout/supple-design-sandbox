package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.Either
import arrow.core.flatMap
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity

class OrderingDslBuilder(
    private val startPreparation: (Drink) -> Unit,
    private val findMenuItem: (DrinkName) -> Either<OrderError, MenuItem>,
    private val hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>,
    private val addInvoice: (Invoice) -> Unit,
    private val steps: MutableList<OrderingDslStep> = mutableListOf()
) : OrderingDsl.ExecutableStep, OrderingDsl.InBetweenStep, OrderingDsl.UnavailableIngredientCheckStep {

    override fun failOnUnavailableIngredient() = this
    override fun withUnknownAsUnavailable() = addStep(FailOnUnavailableIngredientStep(true, hasEnoughStockOf))
    override fun ignoringUnknown() = addStep(FailOnUnavailableIngredientStep(false, hasEnoughStockOf))
    override fun startPreparation() = addStep(StartPreparation(startPreparation))
    override fun issueInvoice() = addStep(IssueInvoiceStep(addInvoice))

    private fun addStep(step: OrderingDslStep): OrderingDsl.ExecutableStep = this.apply { steps += step }

    override fun then(): OrderingDsl.InBetweenStep = this

    override fun applyTo(order: Order) = findMenuItem(order.drink)
        .flatMap { OrderingDslExecutor(remainingSteps = this.steps, order = order, menuItem = it).execute().toEither() }

}