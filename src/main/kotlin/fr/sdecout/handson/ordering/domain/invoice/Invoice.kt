package fr.sdecout.handson.ordering.domain.invoice

import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money

data class Invoice(val lines: List<InvoiceLine>) {

    val totalPrice: Money by lazy { lines.fold(Money.zero(EUR)) { total, line -> total + line.totalPrice } }

    operator fun plus(line: InvoiceLine) = Invoice(lines + line)

    companion object {
        fun from(vararg lines: InvoiceLine) = Invoice(lines.toList())
    }

}