package fr.sdecout.handson.ordering.domain.recipe

import fr.sdecout.annotations.DomainDrivenDesign
import fr.sdecout.annotations.ExceptionalValue
import fr.sdecout.handson.ordering.domain.recipe.UnitOfMeasure.*

@DomainDrivenDesign.ValueObject
sealed class Quantity {

    abstract val amount: Int
    abstract val unitOfMeasure: UnitOfMeasure?

    abstract fun isError(): Boolean

    abstract operator fun times(factor: Scalar): Quantity
    abstract operator fun plus(other: Quantity): Quantity
    abstract operator fun minus(other: Quantity): Quantity
    abstract operator fun compareTo(other: Quantity): Int

    val isZero: Boolean by lazy { amount == 0 }

    companion object {

        @ExceptionalValue
        fun error(): Quantity = Error

        fun Int.milligrams(): Quantity = WithUnit(this, MILLIGRAMS)
        fun Int.grams(): Quantity = (this * 1000).milligrams()

        fun Int.milliliters(): Quantity = WithUnit(this, MILLILITERS)
        fun Int.centiliters(): Quantity = (this * 10).milliliters()

        fun Int.pieces(): Scalar = Scalar(this)

        fun of(amount: Int, unitOfMeasure: UnitOfMeasure?): Quantity = when (unitOfMeasure) {
            MILLILITERS -> amount.milliliters()
            CENTILITERS -> amount.centiliters()
            MILLIGRAMS -> amount.milligrams()
            GRAMS -> amount.grams()
            null -> amount.pieces()
        }

    }

    private object Error : Quantity() {
        override val amount get() = throw IllegalStateException("Invalid instance")
        override val unitOfMeasure get() = throw IllegalStateException("Invalid instance")
        override fun isError() = true
        override operator fun times(factor: Scalar) = Error
        override operator fun plus(other: Quantity) = Error
        override operator fun minus(other: Quantity) = Error
        override operator fun compareTo(other: Quantity): Int = throw IllegalStateException("Invalid instance")
        override fun toString() = "(error)"
    }

    data class Scalar(override val amount: Int) : Quantity() {

        init {
            require(amount >= 0) { "Quantity must not be negative" }
        }

        override val unitOfMeasure get() = null

        override fun isError() = false

        override operator fun times(factor: Scalar) = (this.amount * factor.amount).pieces()

        override operator fun plus(other: Quantity) = other.takeUnless { it.isError() }
            ?.takeIf { it.unitOfMeasure == null }
            ?.takeIf { Int.MAX_VALUE - it.amount > this.amount }
            ?.let { this.amount + it.amount }
            ?.pieces()
            ?: error()

        override operator fun minus(other: Quantity) = other.takeUnless { it.isError() }
            ?.takeIf { it.unitOfMeasure == null }
            ?.let { this.amount - it.amount }
            ?.let { if (it > 0) it else 0 }
            ?.pieces()
            ?: error()

        override operator fun compareTo(other: Quantity): Int = other.takeUnless { it.isError() }
            ?.takeIf { it.unitOfMeasure == null }
            ?.let { this.amount.compareTo(it.amount) }
            ?: throw IllegalArgumentException("Operations do not apply to quantities with inconsistent units of measure (units / ${other.unitOfMeasure})")

        override fun toString() = "$amount pieces"
    }

    private data class WithUnit(override val amount: Int, override val unitOfMeasure: UnitOfMeasure) : Quantity() {

        init {
            require(amount >= 0) { "Quantity must not be negative" }
        }

        override fun isError() = false

        override operator fun times(factor: Scalar) = of(this.amount * factor.amount, unitOfMeasure)

        override operator fun plus(other: Quantity) = other.takeUnless { it.isError() }
            ?.takeIf { it.unitOfMeasure == this.unitOfMeasure }
            ?.takeIf { Int.MAX_VALUE - it.amount > this.amount }
            ?.let { this.amount + it.amount }
            ?.let { of(it, this.unitOfMeasure) }
            ?: error()

        override operator fun minus(other: Quantity) = other.takeUnless { it.isError() }
            ?.takeIf { it.unitOfMeasure == this.unitOfMeasure }
            ?.let { this.amount - it.amount }
            ?.let { if (it > 0) it else 0 }
            ?.let { of(it, this.unitOfMeasure) }
            ?: error()

        override operator fun compareTo(other: Quantity): Int = other.takeUnless { it.isError() }
            ?.takeIf { it.unitOfMeasure == this.unitOfMeasure }
            ?.let { this.amount.compareTo(it.amount) }
            ?: throw IllegalArgumentException("Operations do not apply to quantities with inconsistent units of measure (${this.unitOfMeasure} / ${other.unitOfMeasure})")

        override fun toString() = "$amount${unitOfMeasure.abbreviation}"

    }

}