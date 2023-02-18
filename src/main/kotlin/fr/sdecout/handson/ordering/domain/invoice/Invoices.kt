package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.annotations.HexagonalArchitecture
import fr.sdecout.annotations.HexagonalArchitecture.Port.Type.DRIVEN

@HexagonalArchitecture.Port(DRIVEN)
fun interface Invoices {
    fun add(invoice: Invoice)
}