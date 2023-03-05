package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.dsl.SpecificationDsl
import fr.sdecout.handson.ordering.domain.preparation.DrinkPreparation
import fr.sdecout.handson.ordering.domain.stock.Stock

@DomainDrivenDesign.Service
class OrderingService(preparation: DrinkPreparation, menu: Menu, stock: Stock) : CustomerOrderHandler {

    // TODO: Conceptual contours - Does this design bend in the right places, considering the evolutions listed in the README file?
    private val specification by lazy {
        SpecificationDsl(preparation, menu, stock)
            .failOnUnavailableIngredient().withUnknownAsUnavailable()
            .then().startPreparation()
            .then().issueInvoice()
    }

    override fun process(order: Order) = specification.execute(order)

}

