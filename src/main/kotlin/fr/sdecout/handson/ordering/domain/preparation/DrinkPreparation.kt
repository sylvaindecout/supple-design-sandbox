package fr.sdecout.handson.ordering.domain.preparation

import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVEN
import fr.sdecout.handson.ordering.domain.Order

@HexagonalArchitecture.Port(DRIVEN)
fun interface DrinkPreparation {
    fun queueForPreparation(orderWithRecipe: Order)
}
