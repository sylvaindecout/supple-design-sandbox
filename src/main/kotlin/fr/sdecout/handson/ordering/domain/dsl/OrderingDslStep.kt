package fr.sdecout.handson.ordering.domain.dsl

import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem

interface OrderingDslStep {

    fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?): Pair<OrderError?, Invoice?>

}