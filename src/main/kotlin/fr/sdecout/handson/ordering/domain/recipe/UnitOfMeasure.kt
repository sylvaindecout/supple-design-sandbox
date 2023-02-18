package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.annotations.DomainDrivenDesign

@DomainDrivenDesign.ValueObject
enum class UnitOfMeasure(val abbreviation: String) {

    GRAMS("g"),
    MILLIGRAMS("mg"),
    CENTILITERS("cL"),
    MILLILITERS("mL");

    companion object {
        fun fromAbbreviation(abbreviation: String): UnitOfMeasure? = values().find { it.abbreviation == abbreviation }
    }

}
