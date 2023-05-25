package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent
import fp.serrano.inikio.ProgramBuilder
import fp.serrano.inikio.program


sealed interface MyState<S, out A> {
    data class Finished<S, out A>(val result: A) : MyState<S, A>
    data class Get<S, out A>(val next: (S) -> MyState<S, A>) : MyState<S, A>
    data class Put<S, out A>(val new: S, val next: () -> MyState<S, A>) : MyState<S, A>
}

fun <S, A> MyState<S, A>.execute(initial: S): Pair<S, A> =
    when (this) {
        is MyState.Finished -> initial to result
        is MyState.Get -> next(initial).execute(initial)
        is MyState.Put -> next().execute(new)
    }


typealias EventsState<A> = MyState<List<DomainEvent>, A>
class EventsScope<A> : ProgramBuilder<EventsState<A>, A>({
    result -> MyState.Finished(result)
}) {

    private suspend fun get(): List<DomainEvent> = perform { s -> MyState.Get(s) }

    private suspend fun put(new: List<DomainEvent>): Unit = performUnit { s ->
        MyState.Put(new, s) }

    suspend fun emit(vararg event: DomainEvent) = put(get() + event)

    suspend fun emit(events: List<DomainEvent>) = put(get() + events)

}

fun <A> events(block: suspend EventsScope<A>.() -> A): Pair<List<DomainEvent>, A>
    = program(EventsScope(), block).execute(emptyList())


fun main() {
    val stateExample = increment().execute(0)
    println(stateExample)
}

// state

// TODO: not sure why we need this annotation
//@RestrictsSuspension
class StateScope<S, A> : ProgramBuilder<MyState<S, A>, A>({
        result -> MyState.Finished(result)
}) {

    suspend fun get(): S = perform { arg -> MyState.Get(arg) }

    suspend fun put(new: S): Unit = performUnit { arg ->
        MyState.Put(new, arg) }
}

fun <S, A> state(block: suspend StateScope<S, A>.() -> A): MyState<S, A> =
    program(StateScope(), block)

fun increment(): MyState<Int, String> = state {
    put(get() + 1)
    helloFromScope()
}

context(StateScope<Int, String>)
        suspend fun helloFromScope() : String {
    return "Hello from scope ${get()}"
}

fun main2() {
    val stateExample = increment().execute(0)
    println(stateExample)
}

