package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent
import fp.serrano.inikio.program

@DslMarker
annotation class EventsDsl

@EventsDsl
interface EventsScope {

    /**
     * Emit zero or more events.
     * The events will be appended in order to the list of events already emitted.
     */
    @EventsDsl
    suspend fun emit(vararg event: DomainEvent)

    /**
     * Emit a list containing zero or more events.
     * The events will be appended in order to the list of events already emitted.
     */
    @EventsDsl
    suspend fun emit(events: List<DomainEvent>)
}

class EventsScopeImpl<A>(private val builder: MyStateBuilder<List<DomainEvent>, A>) : EventsScope {

    override suspend fun emit(vararg event: DomainEvent) = builder.put(builder.get() + event)

    override suspend fun emit(events: List<DomainEvent>) = builder.put(builder.get() + events)

}

fun <A> events(block: suspend EventsScope.() -> A): Pair<List<DomainEvent>, A> {
    val builder = MyStateBuilder<List<DomainEvent>, A>()
    return program(builder) {
        val scope = EventsScopeImpl(builder)
        scope.block()
    }.execute(emptyList())
}

fun main() {
    val stateExample = increment().execute(0)
    println(stateExample)
}
