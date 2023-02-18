package fr.sdecout.handson.ordering.domain

import arrow.core.left
import arrow.core.right
import fr.sdecout.handson.ordering.domain.menu.Menu
import fr.sdecout.handson.ordering.domain.menu.MenuItem
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.centiliters
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.grams
import fr.sdecout.handson.ordering.domain.recipe.Recipe
import kotlin.enums.enumEntries

enum class TestMenuItem(val drink: DrinkName, val unitPrice: Money, val recipe: Recipe) {

    LATTE(
        DrinkName("Latte"),
        Money(5.00),
        Recipe.from(
            Ingredient("Coffee beans") to 7.grams(),
            Ingredient("Milk") to 5.centiliters()
        )
    );

    companion object {
        fun testMenu() = Menu { drinkName -> enumEntries<TestMenuItem>()
                .find { it.drink == drinkName }
                ?.let { MenuItem(it.drink, it.unitPrice, it.recipe).right() }
                ?: OrderError.UnknownDrink(drinkName).left()
        }
    }

}
