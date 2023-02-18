package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
@JvmInline
value class Money(val amount: Double) {

    operator fun plus(other: Money) = Money(this.amount + other.amount)

    fun multipliedBy(factor: Long) = Money(amount * factor)

    override fun toString() = "$amount €"

}