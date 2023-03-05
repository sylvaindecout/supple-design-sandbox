package fr.sdecout.handson.ordering.domain.dsl

import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.invoice.InvoiceLine
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.recipe.Quantity

object IssueInvoiceStep : SpecificationStep {

    override fun execute(order: Order, menuItem: MenuItem, invoice: Invoice?) = Pair(null,
        menuItem.toInvoiceWith(order.quantity))

    private fun MenuItem.toInvoiceWith(quantity: Quantity.Scalar): Invoice {
        return Invoice.from(InvoiceLine(
            drink = this.name,
            quantity = quantity,
            unitPrice = this.unitPrice
        )
        )
    }

}