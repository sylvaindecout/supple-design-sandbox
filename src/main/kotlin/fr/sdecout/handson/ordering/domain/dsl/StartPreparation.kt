package fr.sdecout.handson.ordering.domain.dsl

import fr.sdecout.handson.ordering.domain.Customer
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.preparation.Drink
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation

class StartPreparation(private val preparation: DrinkPreparation) : SpecificationStep {

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