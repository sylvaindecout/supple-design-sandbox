package fr.sdecout.handson.ordering.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class DrinkNameTest : ShouldSpec({

    should("fail to initialize from blank name") {
        shouldThrow<IllegalArgumentException> { DrinkName("   ") }
            .message shouldBe "Drink name must not be blank"
    }

    should("display as string") {
        DrinkName("Latte").toString() shouldBe "Latte"
    }

})
