package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.*
import fr.sdecout.handson.ordering.domain.Customer
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity

class OrderingDsl(
    startPreparation: (Drink) -> Unit,
    findMenuItem: (DrinkName) -> Either<OrderError, MenuItem>,
    hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>,
    addInvoice: (Invoice) -> Unit
) {

    private val builder: Builder = Builder(startPreparation, findMenuItem, hasEnoughStockOf, addInvoice)

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

    private class Builder(
        private val startPreparation: (Drink) -> Unit,
        private val findMenuItem: (DrinkName) -> Either<OrderError, MenuItem>,
        private val hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>,
        private val addInvoice: (Invoice) -> Unit,
        private val steps: MutableList<Step> = mutableListOf()
    ) : ExecutableStep, InBetweenStep, UnavailableIngredientCheckStep {

        override fun failOnUnavailableIngredient() = this
        override fun withUnknownAsUnavailable() = addStep(Step.FailOnUnavailableIngredient(true, hasEnoughStockOf))
        override fun ignoringUnknown() = addStep(Step.FailOnUnavailableIngredient(false, hasEnoughStockOf))
        override fun startPreparation() = addStep(Step.StartPreparation(startPreparation))
        override fun issueInvoice() = addStep(Step.IssueInvoice(addInvoice))

        private fun addStep(step: Step): ExecutableStep = this.apply { steps += step }

        override fun then(): InBetweenStep = this

        override fun applyTo(order: Order) = findMenuItem(order.drink)
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

        class FailOnUnavailableIngredient(
            private val unknownAsUnavailable: Boolean,
            private val hasEnoughStockOf: (Ingredient, Quantity) -> Either<OrderError, Boolean>
        ) : Step() {

            override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = menuItem.recipe
                .asMap().toList()
                .map { (ingredient, requiredUnitQuantity) -> ingredient to requiredUnitQuantity * order.quantity }
                .firstOrNull { (ingredient, requiredQuantity) -> !ingredient.isAvailable(requiredQuantity) }
                ?.let { (ingredient, _) -> Pair(OrderError.UnavailableIngredient(ingredient), invoice) }
                ?: Pair(null, invoice)

            private fun Ingredient.isAvailable(requiredQuantity: Quantity) = hasEnoughStockOf(this, requiredQuantity)
                .getOrElse { !unknownAsUnavailable }

        }

        class StartPreparation(private val startPreparation: (Drink) -> Unit) : Step() {

            override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = Pair(null, invoice).also {
                menuItem
                    .asDrinkFor(order.customer)
                    .let { drink -> repeat(order.quantity.amount) { startPreparation(drink) } }
            }

            private fun MenuItem.asDrinkFor(customer: Customer) = Drink(
                name = this.name,
                recipe = this.recipe,
                customer = customer
            )

        }

        class IssueInvoice(private val addInvoice: (Invoice) -> Unit) : Step() {

            override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = menuItem.toInvoiceWith(order.quantity)
                .also(addInvoice)
                .let { Pair(null, it) }

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