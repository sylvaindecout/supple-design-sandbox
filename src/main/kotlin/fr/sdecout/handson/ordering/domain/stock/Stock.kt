package fr.sdecout.handson.ordering.domain.stock

import arrow.core.Either
import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVEN
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.recipe.Ingredient
import fr.sdecout.handson.ordering.domain.recipe.Quantity

@HexagonalArchitecture.Port(DRIVEN)
fun interface Stock {
    fun hasEnoughOf(ingredient: Ingredient, requiredQuantity: Quantity): Either<OrderError.StockAccessFailure, Boolean>
}
