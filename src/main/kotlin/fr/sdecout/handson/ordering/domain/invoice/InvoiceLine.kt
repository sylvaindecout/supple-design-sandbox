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

    fun addTo(invoice: Invoice): Invoice = Invoice(invoice.lines + this)

}
