package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.Either
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation
import fr.sdecout.handson.ordering.domain.stock.Stock

// TODO: Standalone classes - Lots of classes in this package. This could be used to factor some logic... but that's noise.
class SpecificationDsl(preparation: DrinkPreparation, menu: Menu, stock: Stock) {

    private val builder: SpecificationBuilder = SpecificationBuilder(preparation, menu, stock)

    interface ExecutableStep {
        fun then(): InBetweenStep
        fun execute(order: Order): Either<OrderError, Invoice>
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