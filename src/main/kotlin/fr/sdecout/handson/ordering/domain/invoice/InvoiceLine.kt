package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Money
import fr.sdecout.handson.ordering.domain.recipe.Quantity

@DomainDrivenDesign.ValueObject
data class InvoiceLine(
    val drink: DrinkName,
    val quantity: Quantity.Scalar,
    val unitPrice: Money
) {

    val totalPrice: Money by lazy { unitPrice.multipliedBy(quantity.amount.toLong()) }

    // TODO: Closure of operations - Why would you need to know about Invoice in InvoiceLine? What if we want to add some consistency checks between the lines?
    fun addTo(invoice: Invoice): Invoice = Invoice(invoice.lines + this)

}

