package fr.sdecout.handson.ordering.domain.recipe

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class IngredientTest : ShouldSpec({

    should("fail to initialize from blank name") {
        shouldThrow<IllegalArgumentException> { Ingredient("   ") }
            .message shouldBe "Ingredient name must not be blank"
    }

    should("display as string") {
        Ingredient("Coffee beans").toString() shouldBe "Coffee beans"
    }

})
