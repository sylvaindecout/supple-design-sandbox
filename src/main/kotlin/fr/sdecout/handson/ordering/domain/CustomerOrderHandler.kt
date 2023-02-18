package fr.sdecout.handson.ordering.domain

import arrow.core.Either
import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVING
import fr.sdecout.handson.ordering.domain.invoice.Invoice

@HexagonalArchitecture.Port(DRIVING)
interface CustomerOrderHandler {
    fun orchestrateCallsToOtherServicesFor(order: Order): Either<OrderError, Invoice>
}
