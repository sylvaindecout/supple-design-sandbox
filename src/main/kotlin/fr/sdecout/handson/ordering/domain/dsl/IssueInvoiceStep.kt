package fr.sdecout.handson.ordering.domain.dsl

import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.recipe.Quantity

class IssueInvoiceStep(
    private val addInvoice: (Invoice) -> Unit
) : OrderingDslStep {

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