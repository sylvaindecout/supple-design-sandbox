package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.handson.ordering.domain.recipe.UnitOfMeasure.GRAMS
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class UnitOfMeasureTest : ShouldSpec({

    should("initialize from existing abbreviation") {
        UnitOfMeasure.fromAbbreviation("g") shouldBe GRAMS
    }

    should("fail to initialize from unknown abbreviation") {
        UnitOfMeasure.fromAbbreviation("unknown") shouldBe null
    }

})