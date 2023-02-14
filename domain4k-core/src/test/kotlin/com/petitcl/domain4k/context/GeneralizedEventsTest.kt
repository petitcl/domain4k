package com.petitcl.domain4k.context

import com.petitcl.domain4k.fixtures.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class GeneralizedEventsTest {

    @Test
    fun `within with piped events context should collect all events raised in block`() {
        val collector = CollectingEventsContext()
        within(collector.pipe()) {
            publishEvent(TestEvent("TestEvent1"))
            publishEvents(listOf(TestEvent("TestEvent2"), TestEvent("TestEvent3")))
        }
        val events = collector.events()

        assertThat(events)
            .containsExactly(
                TestEvent("TestEvent1"),
                TestEvent("TestEvent2"),
                TestEvent("TestEvent3"),
            )
    }

    @Test
    fun `within with piped events context should publish events after the block is finished`() {
        val collector = TestEventsContext()
        within(collector.pipe()) {
            publishEvent(TestEvent("TestEvent1"))
            publishEvent(TestEvent("TestEvent2"))
        }
        val events = collector.events()

        assertThat(events)
            .containsExactly(
                TestEvent("TestEvent1"),
                TestEvent("TestEvent2"),
                TestEvent("TestEventContext.publishEvents"),
            )
    }

    @Test
    fun `within with piped events context should allow to use additional contexts in block`() {
        val collector = CollectingEventsContext()
        within(collector.pipe(), A(), B()) {
            publishEvent(TestEvent(doA()))
            publishEvent(TestEvent(doB()))
        }
        val events = collector.events()

        assertThat(events)
            .containsExactly(
                TestEvent("contextA"),
                TestEvent("contextB")
            )
    }

}