package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign

// TODO: Drawing on established formalisms - How will you deal with currencies, fixed number of digits, etc.? By chance, some formalisms exist to represent money, including notably Money design pattern.
@DomainDrivenDesign.ValueObject
@JvmInline
value class Money(val amount: Double) {

    operator fun plus(other: Money) = Money(this.amount + other.amount)

    fun multipliedBy(factor: Long) = Money(amount * factor)

    override fun toString() = "$amount €"

}