package com.petitcl.domain4k.context

import com.petitcl.domain4k.stereotype.DomainEvent
import com.petitcl.domain4k.stereotype.WithEvents
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Context for raising events from domain
 */
interface EventsContext {
    fun publishEvent(event: DomainEvent)
    fun publishEvents(event: List<DomainEvent>) = event.forEach { publishEvent(it) }
    fun publishEventsOf(withEvents: WithEvents<*>) = publishEvents(withEvents.events)

    companion object
}

class NoOpEventsContext : EventsContext {
    override fun publishEvent(event: DomainEvent) = Unit
}

fun EventsContext.Companion.noOp(): EventsContext = NoOpEventsContext()

class CollectingEventsContext : EventsContext {
    private val events = mutableListOf<DomainEvent>()
    fun events() = events.toList()
    override fun publishEvent(event: DomainEvent) {
        events.add(event)
    }
    override fun publishEvents(event: List<DomainEvent>) {
        events.addAll(event)
    }
}

/**
 * Return a new [EventsContext] that will forward all events to this [EventsContext] after the block is done executing.
 * Use in conjunction with [within] to collect events raised in a block of code.
 */
fun EventsContext.pipe(): EventsContext = ForwardingEventsContext(this)

class ForwardingEventsContext(
    private val sink: EventsContext
) : EventsContext, FinalizingContext {
    private val collector = CollectingEventsContext()
    override fun publishEvent(event: DomainEvent) = collector.publishEvent(event)
    override fun finalize() = sink.publishEvents(collector.events())
}

/**
 * Run a block of code and collect all events raised during the execution.
 * Events collected are flushed to the given publisher after the block is done executing.
 */
@OptIn(ExperimentalContracts::class)
inline fun <R> runAndCollectEvents(sink: EventsContext, block: context(EventsContext) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val collector = CollectingEventsContext()
    val r = block(collector)
    val events = collector.events()

    sink.publishEvents(events)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, R> runAndCollectEvents(sink: EventsContext, a: A, block: context(EventsContext, A) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val collector = CollectingEventsContext()
    val r = block(collector, a)
    val events = collector.events()

    sink.publishEvents(events)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, R> runAndCollectEvents(sink: EventsContext, a: A, b: B, block: context(EventsContext, A, B) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val collector = CollectingEventsContext()
    val r = block(collector, a, b)
    val events = collector.events()

    sink.publishEvents(events)
    return r
}

// TODO: delete runAndReturnEvents?

/**
 * Run a block of code and collect all events raised during the execution.
 * Events collected are returned as a list.
 */
@OptIn(ExperimentalContracts::class)
inline fun runAndReturnEvents(block: context(EventsContext) () -> Unit): List<DomainEvent> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val collector = CollectingEventsContext()
    block(collector)

    return collector.events()
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A> runAndReturnEvents(a: A, block: context(EventsContext, A) () -> Unit): List<DomainEvent> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val collector = CollectingEventsContext()
    block(collector, a)

    return collector.events()
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B> runAndReturnEvents(a: A, b : B, block: context(EventsContext, A,B) () -> Unit): List<DomainEvent> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val collector = CollectingEventsContext()
    block(collector, a, b)

    return collector.events()
}
