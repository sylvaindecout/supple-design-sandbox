package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.flatMap
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation
import fr.sdecout.handson.ordering.domain.stock.Stock

class SpecificationBuilder(
    private val preparation: DrinkPreparation,
    private val menu: Menu,
    private val stock: Stock,
    private val steps: MutableList<SpecificationStep> = mutableListOf()
) : SpecificationDsl.ExecutableStep, SpecificationDsl.InBetweenStep, SpecificationDsl.UnavailableIngredientCheckStep {

    override fun failOnUnavailableIngredient() = this
    override fun withUnknownAsUnavailable() = addStep(FailOnUnavailableIngredientStep(true, stock))
    override fun ignoringUnknown() = addStep(FailOnUnavailableIngredientStep(false, stock))
    override fun startPreparation() = addStep(StartPreparation(preparation))
    override fun issueInvoice() = addStep(IssueInvoiceStep)

    private fun addStep(step: SpecificationStep): SpecificationDsl.ExecutableStep = this.apply { steps += step }

    override fun then(): SpecificationDsl.InBetweenStep = this

    override fun execute(order: Order) = menu.find(order.drink)
        .flatMap { SpecificationExecutor(remainingSteps = this.steps, order = order, menuItem = it).execute().toEither() }

}