package fr.sdecout.handson.ordering.domain

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
@JvmInline
value class Customer(val name: String) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("Customer name must not be blank")
        }
    }

    override fun toString() = name

}
