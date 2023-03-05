package fr.sdecout.handson.ordering.domain

import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import fr.sdecout.handson.ordering.domain.recipe.Recipe
import fr.sdecout.handson.ordering.domain.recipe.UnitOfMeasure
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class OrderTest : ShouldSpec({

    should("always have a recipe after call to withRecipe") {
        checkAll(Arb.order(), Arb.recipe()) { order, recipe ->
            val updatedOrder = order.withRecipe(recipe)
            updatedOrder.recipe shouldNotBe null
        }
    }

})

fun Arb.Companion.quantity() = Arb.bind(
    Arb.positiveInt(10000),
    Arb.enum<UnitOfMeasure>().orNull(),
    Quantity.Companion::of
)

fun Arb.Companion.recipe(): Arb<Recipe> = Arb.map(
    Arb.string().filter { it.isNotBlank() }.map { Ingredient(it) },
    Arb.quantity(),
    minSize = 1
).map { Recipe(it) }

fun Arb.Companion.order(): Arb<Order> = Arb.bind(
    Arb.string().filter { it.isNotBlank() }.map { DrinkName(it) },
    Arb.positiveInt(100).map { it.pieces() },
    Arb.string().filter { it.isNotBlank() }.map { Customer(it) },
    Arb.recipe().orNull(),
    ::Order
)