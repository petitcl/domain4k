@file:Suppress("unused")

package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent

data class State<S, out A>(val run: (S) -> Pair<A, S>) {

    companion object {

        fun <S> set(s: S): State<S, Unit> =
            State { _: S -> Unit to s }

        fun <S, A> unit(a: A): State<S, A> =
            State { s: S -> a to s }

        fun <S, A, B, C> map2(
            ra: State<S, A>,
            rb: State<S, B>,
            f: (A, B) -> C
        ): State<S, C> =
            ra.flatMap { a ->
                rb.map { b ->
                    f(a, b)
                }
            }
    }

    fun <B> map(f: (A) -> B): State<S, B> =
        flatMap { a -> unit(f(a)) }

    fun <B> flatMap(f: (A) -> State<S, B>): State<S, B> =
        State { s: S ->
            val (a: A, s2: S) = this.run(s)
            f(a).run(s2)
        }
}

typealias MyMonad<R, E> = State<List<E>, R>

fun <R, E> emitAndReturn(value: R, vararg emittedValues: E): MyMonad<R, E> =
    State { emitted ->
        value to emitted + emittedValues.toList()
    }

class MyEventMonad<A> private constructor(private val state: State<List<DomainEvent>, A>) {
    companion object {
        fun create(): MyEventMonad<Unit> =
            MyEventMonad(State.set(emptyList()))
    }

    fun <B> map(f: (A) -> B): MyEventMonad<B> = MyEventMonad(state.map(f))

    fun <B> flatMap(f: (A) -> Pair<B, List<DomainEvent>>): MyEventMonad<B>
        = MyEventMonad(state.flatMap {
            val (newValue, newEvents) = f(it)
            State { events ->
                newValue to events + newEvents.toList()
            }
        })

    fun addEvents(f: () -> List<DomainEvent>): MyEventMonad<A>
        = flatMap { it to f() }

    fun run(): Pair<A, List<DomainEvent>> = state.run(emptyList())
}


