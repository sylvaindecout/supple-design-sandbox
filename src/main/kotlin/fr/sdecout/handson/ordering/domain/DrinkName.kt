package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
@JvmInline
value class DrinkName(val value: String) {

    init {
        require(value.isNotBlank()) { "Drink name must not be blank" }
    }

    override fun toString() = value

}
