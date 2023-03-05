package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.Either
import arrow.core.flatMap
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity

class SpecificationBuilder(
    private val startPreparation: (Drink) -> Unit,
    private val findMenuItem: (DrinkName) -> Either<OrderError, MenuItem>,
    private val hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>,
    private val steps: MutableList<SpecificationStep> = mutableListOf()
) : SpecificationDsl.ExecutableStep, SpecificationDsl.InBetweenStep, SpecificationDsl.UnavailableIngredientCheckStep {

    override fun failOnUnavailableIngredient() = this
    override fun withUnknownAsUnavailable() = addStep(FailOnUnavailableIngredientStep(true, hasEnoughStockOf))
    override fun ignoringUnknown() = addStep(FailOnUnavailableIngredientStep(false, hasEnoughStockOf))
    override fun startPreparation() = addStep(StartPreparation(startPreparation))
    override fun issueInvoice() = addStep(IssueInvoiceStep)

    private fun addStep(step: SpecificationStep): SpecificationDsl.ExecutableStep = this.apply { steps += step }

    override fun then(): SpecificationDsl.InBetweenStep = this

    override fun execute(order: Order) = findMenuItem(order.drink)
        .flatMap { SpecificationExecutor(remainingSteps = this.steps, order = order, menuItem = it).execute().toEither() }

}