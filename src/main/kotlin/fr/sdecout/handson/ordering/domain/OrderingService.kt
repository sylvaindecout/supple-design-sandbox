package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.dsl.OrderingDsl
import fr.sdecout.handson.ordering.domain.invoice.Invoices
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation
import fr.sdecout.handson.ordering.domain.stock.Stock

@DomainDrivenDesign.Service
class OrderingService(preparation: DrinkPreparation, menu: Menu, stock: Stock, invoices: Invoices) : CustomerOrderHandler {

    // TODO: Conceptual contours - Does this design bend in the right places, considering the evolutions listed in the README file?
    private val specification by lazy {
        OrderingDsl(preparation::queueForPreparation, menu::find, stock::hasEnoughOf, invoices::add)
            .failOnUnavailableIngredient().withUnknownAsUnavailable()
            .then().startPreparation()
            .then().issueInvoice()
    }

    override fun process(order: Order) = specification.applyTo(order)

}

