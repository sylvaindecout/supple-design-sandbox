package fr.sdecout.handson.ordering.domain.preparation

import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVEN
import fr.sdecout.handson.ordering.domain.Order

@HexagonalArchitecture.Port(DRIVEN)
fun interface DrinkPreparation {
    // TODO: Assertions - How could I be sure that Order has recipe set at this stage?
    fun queueForPreparation(orderWithRecipe: Order)
}
