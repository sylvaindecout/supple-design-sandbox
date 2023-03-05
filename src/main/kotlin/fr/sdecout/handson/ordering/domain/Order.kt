package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.recipe.Quantity

@DomainDrivenDesign.ValueObject
data class Order(
    val drink: DrinkName,
    val quantity: Quantity.Scalar,
    val customer: Customer
)
