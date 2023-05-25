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

class MyStateBuilder<S, A> : ProgramBuilder<MyState<S, A>, A>({
    result -> MyState.Finished(result)
}) {
    suspend fun get(): S = perform { s -> MyState.Get(s) }

    suspend fun put(new: S): Unit = performUnit { s ->
        MyState.Put(new, s) }

}

interface EventsScope {
    suspend fun emit(vararg event: DomainEvent)
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

