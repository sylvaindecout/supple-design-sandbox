package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.handson.ordering.domain.recipe.UnitOfMeasure.GRAMS
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

class UnitOfMeasureTest {

    @Test
    fun should_initialize_from_existing_abbreviation() {
        val abbreviation = "g"

        val unitOfMeasure = UnitOfMeasure.fromAbbreviation(abbreviation)

        expectThat(unitOfMeasure).isEqualTo(GRAMS)
    }

    @Test
    fun should_fail_to_initialize_from_unknown_abbreviation() {
        val abbreviation = "unknown"

        val unitOfMeasure = UnitOfMeasure.fromAbbreviation(abbreviation)

        expectThat(unitOfMeasure).isNull()
    }

}