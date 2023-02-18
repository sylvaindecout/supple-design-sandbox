package fr.sdecout.handson.ordering.domain.preparation

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.Customer
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.recipe.Recipe

@DomainDrivenDesign.ValueObject
data class Drink(
    val name: DrinkName,
    val recipe: Recipe?,
    val customer: Customer
) {
    init {
        require(recipe != null)
    }
}