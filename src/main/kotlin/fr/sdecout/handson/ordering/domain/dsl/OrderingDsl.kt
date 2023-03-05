package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.Either
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity

// TODO: Standalone classes - Lots of classes in this package. This could be used to factor some logic... but that's noise.
// TODO: Standalone classes - We could try to cut on dependencies by replacing Quantity with Int, for instance. Why would it be bad?
class OrderingDsl(
    startPreparation: (Drink) -> Unit,
    findMenuItem: (DrinkName) -> Either<OrderError, MenuItem>,
    hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>,
    addInvoice: (Invoice) -> Unit
) {

    private val builder: OrderingDslBuilder = OrderingDslBuilder(startPreparation, findMenuItem, hasEnoughStockOf, addInvoice)

    interface ExecutableStep {
        fun then(): InBetweenStep
        fun applyTo(order: Order): Either<OrderError, Invoice>
    }

    interface InBetweenStep {
        fun failOnUnavailableIngredient(): UnavailableIngredientCheckStep
        fun startPreparation(): ExecutableStep
        fun issueInvoice(): ExecutableStep
    }

    interface UnavailableIngredientCheckStep {
        fun withUnknownAsUnavailable(): ExecutableStep
        fun ignoringUnknown(): ExecutableStep
    }

    fun failOnUnavailableIngredient(): UnavailableIngredientCheckStep {
        return builder.failOnUnavailableIngredient()
    }

    fun startPreparation(): ExecutableStep {
        return builder.startPreparation()
    }

    fun issueInvoice(): ExecutableStep {
        return builder.issueInvoice()
    }

}