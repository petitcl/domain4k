package com.petitcl.domain4k.context

import com.petitcl.domain4k.fixtures.*
import com.petitcl.domain4k.stereotype.DomainEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class EventsTest {

    @Test
    fun `runAndCollectEvents should collect all events raised in block`() {
        val collector = CollectingEventsContext()
        runAndCollectEvents(collector) {
            publishEvent(TestEvent("TestEvent1"))
            publishEvents(listOf(TestEvent("TestEvent2"), TestEvent("TestEvent3")))
        }
        val events = collector.events()

        assertThat(events)
            .containsExactly(
                TestEvent("TestEvent1"),
                TestEvent("TestEvent2"),
                TestEvent("TestEvent3")
            )
    }

    @Test
    fun `runAndCollectEvents should publish events after the block is finished`() {
        val collector = TestEventsContext()
        runAndCollectEvents(collector) {
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
    fun `runAndCollectEvents should allow to use additional contexts in block`() {
        val collector = CollectingEventsContext()
        runAndCollectEvents(collector, A(), B()) {
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

    @Test
    fun `runAndCollectEvents should forward return value`() {
        val collector = CollectingEventsContext()
        val result = runAndCollectEvents(collector) {
            42
        }

        assertThat(result).isEqualTo(42)
    }

    @Test
    fun `runAndReturnEvents should return all events raised in block`() {
        val events = runAndReturnEvents {
            publishEvent(TestEvent("TestEvent1"))
            publishEvents(listOf(TestEvent("TestEvent2"), TestEvent("TestEvent3")))
        }

        assertThat(events)
            .containsExactly(
                TestEvent("TestEvent1"),
                TestEvent("TestEvent2"),
                TestEvent("TestEvent3"),
            )
    }

    @Test
    fun `runAndReturnEvents should allow to use additional contexts in block`() {
        val events =  runAndReturnEvents(A(), B()) {
            publishEvent(TestEvent(doA()))
            publishEvent(TestEvent(doB()))
        }

        assertThat(events)
            .containsExactly(
                TestEvent("contextA"),
                TestEvent("contextB")
            )
    }

}