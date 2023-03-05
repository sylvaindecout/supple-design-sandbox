package fr.sdecout.handson.ordering.domain.preparation

import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVEN

@HexagonalArchitecture.Port(DRIVEN)
fun interface DrinkPreparation {
    fun queueForPreparation(drink: Drink)
}
