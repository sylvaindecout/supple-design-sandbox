package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.annotations.DomainDrivenDesign
import kotlin.enums.enumEntries

@DomainDrivenDesign.ValueObject
enum class UnitOfMeasure(val abbreviation: String) {

    GRAMS("g"),
    MILLIGRAMS("mg"),
    CENTILITERS("cL"),
    MILLILITERS("mL");

    companion object {
        fun fromAbbreviation(abbreviation: String): UnitOfMeasure? = enumEntries<UnitOfMeasure>()
            .find { it.abbreviation == abbreviation }
    }

}
