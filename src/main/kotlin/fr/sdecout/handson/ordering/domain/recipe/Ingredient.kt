package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
@JvmInline
value class Ingredient(val name: String) {

    init {
        require(name.isNotBlank()) { "Ingredient name must not be blank" }
    }

    override fun toString() = name

}
