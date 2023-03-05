package fr.sdecout.handson.ordering.domain.invoice

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.handson.ordering.domain.Money

@DomainDrivenDesign.ValueObject
data class Invoice(val lines: List<InvoiceLine>) {

    val totalPrice: Money by lazy { lines.fold(Money(0.0)) { total, line -> total + line.totalPrice } }

    operator fun plus(line: InvoiceLine) = Invoice(lines + line)

    companion object {
        fun from(vararg lines: InvoiceLine) = Invoice(lines.toList())
    }

}