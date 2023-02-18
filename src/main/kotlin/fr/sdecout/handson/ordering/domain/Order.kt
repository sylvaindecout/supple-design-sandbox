package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import fr.sdecout.handson.ordering.domain.recipe.Recipe

@DomainDrivenDesign.ValueObject
data class Order(
    val drink: DrinkName,
    val quantity: Quantity.Scalar,
    val customer: Customer,
    val recipe: Recipe? = null
) {

    fun withRecipe(recipe: Recipe) = this.copy(recipe = recipe)

}