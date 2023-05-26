package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent
import fp.serrano.inikio.program


class EventsContextImpl<A>(
    private val builder: MyStateBuilder<List<DomainEvent>, A>,
    private val onEvent: suspend (DomainEvent) -> Unit = {},
) : EventsContext {

    override suspend fun publish(vararg event: DomainEvent) {
        println(builder.get())
        builder.put(builder.get() + event)
        event.forEach { onEvent(it) }
    }

    override suspend fun publish(events: List<DomainEvent>) {
        println(builder.get())
        builder.put(builder.get() + events)
        events.forEach { onEvent(it) }
    }

}

/**
 * Execute a block of code that can publish events.
 * The events will be collected by the provided [EventsCollector] and then published.
 */
suspend fun <A> events(
    collector: EventsCollector,
    block: suspend EventsContext.() -> A,
): A {
    val builder = MyStateBuilder<List<DomainEvent>, A>()
    val (events, result) = program(builder) {
        val scope = EventsContextImpl(builder)
        scope.block()
    }.execute(emptyList())
    events.forEach { collector.publish(it) }
    return result
}

/**
 * Execute a block of code that can publish events.
 * The events will be collected by the provided [EventsCollector] and published eagerly.
 * As soon as the DSL publish is called, the events will be handled immediately
 */
suspend fun <A> eventsEager(
    collector: EventsCollector,
    block: suspend EventsContext.() -> A,
): A {
    val builder = MyStateBuilder<List<DomainEvent>, A>()
    val onEvent: suspend (DomainEvent) -> Unit = { collector.publish(it) }
    val (_, result) = program(builder) {
        val scope = EventsContextImpl(builder, onEvent)
        scope.block()
    }.execute(emptyList())
    return result
}

/**
 * Execute a block of code that can publish events.
 * The events will be collected in memory and returned along with the result of the block.
 */
fun <A> events(block: suspend EventsContext.() -> A): Pair<List<DomainEvent>, A> {
    val builder = MyStateBuilder<List<DomainEvent>, A>()
    return program(builder) {
        val scope = EventsContextImpl(builder)
        scope.block()
    }.execute(emptyList())
}
