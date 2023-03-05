package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.*
import fr.sdecout.handson.ordering.domain.Customer
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import fr.sdecout.handson.ordering.domain.stock.Stock

class SpecificationDsl(preparation: DrinkPreparation, menu: Menu, stock: Stock) {

    private val builder: Builder = Builder(preparation, menu, stock)

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

    private class Builder(
        private val preparation: DrinkPreparation,
        private val menu: Menu,
        private val stock: Stock,
        private val steps: MutableList<Step> = mutableListOf()
    ) : ExecutableStep, InBetweenStep, UnavailableIngredientCheckStep {

        override fun failOnUnavailableIngredient() = this
        override fun withUnknownAsUnavailable() = addStep(Step.FailOnUnavailableIngredient(true, stock))
        override fun ignoringUnknown() = addStep(Step.FailOnUnavailableIngredient(false, stock))
        override fun startPreparation() = addStep(Step.StartPreparation(preparation))
        override fun issueInvoice() = addStep(Step.IssueInvoice)

        private fun addStep(step: Step): ExecutableStep = this.apply { steps += step }

        override fun then(): InBetweenStep = this

        override fun execute(order: Order) = menu.find(order.drink)
            .flatMap { Executor(remainingSteps = this.steps, order = order, menuItem = it).execute().toEither() }

    }

    private class Executor(
        private val remainingSteps: List<Step>,
        private val order: Order,
        private val menuItem: MenuItem,
        private val error: OrderError? = null,
        private val invoice: Invoice? = null
    ) {

        fun toEither(): Either<OrderError, Invoice> = error?.left() ?: invoice!!.right()

        fun execute(): Executor {
            if (remainingSteps.isEmpty() || error != null) {
                return this
            }
            return remainingSteps.split()!!.let {(tail, first) ->
                first.execute(order, menuItem, invoice)
                    .let { (error, invoice) ->
                        Executor(
                            remainingSteps = tail,
                            order = order,
                            menuItem = menuItem,
                            error = error,
                            invoice = invoice
                        ).execute()
                    }
            }
        }

    }

    private sealed class Step {

        abstract fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?): Pair<OrderError?, Invoice?>

        class FailOnUnavailableIngredient(private val unknownAsUnavailable: Boolean, private val stock: Stock) : Step() {

            override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = menuItem.recipe
                .asMap().toList()
                .map { (ingredient, requiredUnitQuantity) -> ingredient to requiredUnitQuantity * order.quantity }
                .firstOrNull { (ingredient, requiredQuantity) -> !ingredient.isAvailable(requiredQuantity) }
                ?.let { (ingredient, _) -> Pair(OrderError.UnavailableIngredient(ingredient), invoice) }
                ?: Pair(null, invoice)

            private fun Ingredient.isAvailable(requiredQuantity: Quantity) = stock.hasEnoughOf(this, requiredQuantity)
                .getOrElse { !unknownAsUnavailable }

        }

        class StartPreparation(private val preparation: DrinkPreparation) : Step() {

            override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = Pair(null, invoice).also {
                menuItem
                    .asDrinkFor(order.customer)
                    .let { drink -> repeat(order.quantity.amount) { preparation.queueForPreparation(drink) } }
            }

            private fun MenuItem.asDrinkFor(customer: Customer) = Drink(
                name = this.name,
                recipe = this.recipe,
                customer = customer
            )

        }

        object IssueInvoice : Step() {

            override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = Pair(null,
                menuItem.toInvoiceWith(order.quantity))

            private fun MenuItem.toInvoiceWith(quantity: Quantity.Scalar): Invoice {
                return Invoice.from(InvoiceLine(
                    drink = this.name,
                    quantity = quantity,
                    unitPrice = this.unitPrice
                ))
            }

        }

    }

}