package com.petitcl.sample.domain

import com.petitcl.domain4k.context.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

val time = TimeContext.default()
val events = EventsContext.noOp()
internal class CartKtTest {

    @Test
    fun `test create new cart with events context`() {
        val events = runAndReturnEvents(time) {
            val cart = Cart.newCart()
            assertNotNull(cart.id)
        }
        assertEquals(1, events.size)
        assertTrue(events[0] is CartCreated)
    }

    @Test
    fun `test create new cart with multiple context`() {
        within(events, time) {
            val cart = Cart.newCart()
            assertNotNull(cart.id)
        }
    }

}
