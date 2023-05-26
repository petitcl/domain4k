package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent

@DslMarker
annotation class EventsDsl

@EventsDsl
interface EventsContext {

    /**
     * Publish zero or more events.
     * The events will be appended in order to the list of events already emitted.
     */
    @EventsDsl
    suspend fun publish(vararg event: DomainEvent)

    /**
     * Publish a list containing zero or more events.
     * The events will be appended in order to the list of events already emitted.
     */
    @EventsDsl
    suspend fun publish(events: List<DomainEvent>)
}

/**
 * Base interface for classes that can collect and forward domain events.
 */
interface EventsCollector {
    suspend fun publish(event: DomainEvent)
}

object EventsCollectors {
    /**
     * [EventsCollector] that does not do anything.
     */
    val noOp = NoOpEventsCollector

    /**
     * [EventsCollector] that collects events in memory and allows to retrieve them.
     */
    fun inMemory() = InMemoryEventsCollector()
}

object NoOpEventsCollector : EventsCollector {
    override suspend fun publish(event: DomainEvent) = Unit
}

class InMemoryEventsCollector : EventsCollector {
    private val _events = mutableListOf<DomainEvent>()

    val events: List<DomainEvent>
            get () = _events.toList()
    override suspend fun publish(event: DomainEvent) {
        _events.add(event)
    }

}

class EagerEventsCollector(
    private val sink: EventsCollector,
) : EventsCollector {

    override suspend fun publish(event: DomainEvent) {
        sink.publish(event)
    }

}