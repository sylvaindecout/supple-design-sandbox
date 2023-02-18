package fr.sdecout.handson.ordering.domain.menu

import arrow.core.Either
import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVEN
import fr.sdecout.handson.ordering.domain.DrinkName
import fr.sdecout.handson.ordering.domain.OrderError

@HexagonalArchitecture.Port(DRIVEN)
fun interface Menu {
    fun find(drink: DrinkName): Either<OrderError.UnknownDrink, MenuItem>
}
