package fr.sdecout.handson.ordering.domain.dsl

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.split
import fr.sdecout.handson.ordering.domain.Order
import fr.sdecout.handson.ordering.domain.OrderError
import fr.sdecout.handson.ordering.domain.invoice.Invoice
import fr.sdecout.handson.ordering.domain.menu.MenuItem

class OrderingDslExecutor(
    private val remainingSteps: List<OrderingDslStep>,
    private val order: Order,
    private val menuItem: MenuItem,
    private val error: OrderError? = null,
    private val invoice: Invoice? = null
) {

    fun toEither(): Either<OrderError, Invoice> = error?.left() ?: invoice!!.right()

    fun execute(): OrderingDslExecutor {
        if (remainingSteps.isEmpty() || error != null) {
            return this
        }
        return remainingSteps.split()!!.let {(tail, first) ->
            first.execute(order, menuItem, invoice)
                .let { (error, invoice) ->
                    OrderingDslExecutor(
                        remainingSteps = tail,
                        order = order,
                        menuItem = menuItem,
                        error = error,
                        invoice = invoice
                    ).execute()
                }
        }
    }

}