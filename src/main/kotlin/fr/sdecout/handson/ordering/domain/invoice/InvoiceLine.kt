package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import org.joda.money.Money

@DomainDrivenDesign.ValueObject
data class InvoiceLine(
    val drink: DrinkName,
    val quantity: Quantity.Scalar,
    val unitPrice: Money
) {

    val totalPrice: Money by lazy { unitPrice.multipliedBy(quantity.amount.toLong()) }

}

