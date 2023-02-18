package fr.sdecout.handson.ordering.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CustomerTest : ShouldSpec({

    should("fail to initialize from blank name") {
        shouldThrow<IllegalArgumentException> { Customer("   ") }
            .message shouldBe "Customer name must not be blank"
    }

    should("display as string") {
        Customer("Vincent").toString() shouldBe "Vincent"
    }

})
