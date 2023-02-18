package fr.sdecout.handson.ordering.domain.menu

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.Money
import fr.sdecout.handson.ordering.domain.recipe.Recipe

@DomainDrivenDesign.Entity
data class MenuItem(
    @DomainDrivenDesign.Entity.Id val name: DrinkName,
    val unitPrice: Money,
    val recipe: Recipe
)
