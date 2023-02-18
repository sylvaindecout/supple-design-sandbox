package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.centiliters
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.error
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.grams
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.milligrams
import fr.sdecout.handson.ordering.domain.recipe.Quantity.Companion.pieces
import fr.sdecout.handson.ordering.domain.recipe.UnitOfMeasure.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

private val aValidQuantity = Quantity.of(3, GRAMS)

class QuantityTest : ShouldSpec({

    should("fail to initialize from negative amount") {
        shouldThrow<IllegalArgumentException> { Quantity.of(-1, GRAMS) }
            .message shouldBe "Quantity must not be negative"
    }

    should("identify quantity with 0 amount as zero") {
        Quantity.of(0, GRAMS).isZero shouldBe true
    }

    should("not identify quantity with positive amount as zero") {
        Quantity.of(1, GRAMS).isZero shouldBe false
    }

    context("multiply") {
        should("multiply") {
            3.grams() * 2.pieces() shouldBe 6.grams()
        }

        should("fail to multiply error") {
            error() * 2.pieces() shouldBe error()
        }
    }

    context("add") {
        should("add") {
            3.grams() + 2.grams() shouldBe 5.grams()
        }

        should("fail to add if result is above max") {
            val max = Int.MAX_VALUE.milligrams()

            val result = max + 1.milligrams()

            result.isError() shouldBe true
        }

        should("fail to add if units are inconsistent") {
            val mass = 3.grams()
            val volume = 2.centiliters()

            val result = mass + volume

            result.isError() shouldBe true
        }

        should("fail to add error") {
            aValidQuantity + error() shouldBe error()
        }

        should("fail to add from error") {
            error() + aValidQuantity shouldBe error()
        }
    }

    context("subtract") {
        should("subtract") {
            3.grams() - 2.grams() shouldBe 1.grams()
        }

        should("subtract greater value to 0") {
            3.grams() - 10.grams() shouldBe 0.grams()
        }

        should("fail to subtract if units are inconsistent") {
            val mass = 3.grams()
            val volume = 2.centiliters()

            val result = mass - volume

            result.isError() shouldBe true
        }

        should("fail to subtract error") {
            aValidQuantity - error() shouldBe error()
        }

        should("fail to subtract from error") {
            error() - aValidQuantity shouldBe error()
        }
    }

    context("compare") {
        should("compare quantities") {
            (3.grams() > 10.grams()) shouldBe false
        }

        should("fail to compare quantities if units are inconsistent") {
            val mass = 3.grams()
            val volume = 2.centiliters()

            shouldThrow<IllegalArgumentException> { mass > volume }
                .message shouldBe "Operations do not apply to quantities with inconsistent units of measure ($MILLIGRAMS / $MILLILITERS)"
        }

        should("fail to compare quantities if this is an error") {
            shouldThrow<IllegalStateException> { error() > aValidQuantity }
                .message shouldBe "Invalid instance"
        }

        should("fail to compare quantities if other is an error") {
            shouldThrow<IllegalStateException> { aValidQuantity > error() }
                .message shouldBe "Invalid instance"
        }
    }

    context("toString") {
        should("display as string") {
            3.grams().toString() shouldBe "3000mg"
        }

        should("display as string with no unit of measure") {
            Quantity.of(3, null).toString() shouldBe "3 pieces"
        }

        should("display error as string") {
            error().toString() shouldBe "(error)"
        }
    }

})
