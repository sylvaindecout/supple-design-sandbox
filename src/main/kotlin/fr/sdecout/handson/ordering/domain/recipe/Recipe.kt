package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
data class Recipe(private val entries: Map<Ingredient, Quantity>) {

    init {
        require(entries.isNotEmpty()) { "Recipe must not be empty" }
        require(entries.values.none { it.isZero }) { "Quantities in a recipe must not be 0" }
    }

    fun asMap(): Map<Ingredient, Quantity> = entries

    companion object {
        fun from(vararg pairs: Pair<Ingredient, Quantity>) = Recipe(pairs.toMap())
    }

}
