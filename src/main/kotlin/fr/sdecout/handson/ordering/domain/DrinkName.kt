package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
@JvmInline
value class DrinkName(val value: String) {

    init {
        if (value.isBlank()) {
            throw IllegalArgumentException("Drink name must not be blank")
        }
    }

    override fun toString() = value

}
