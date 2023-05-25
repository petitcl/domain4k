package com.petitcl.domain4k.experimental

import fp.serrano.inikio.ProgramBuilder


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

// TODO: not sure why we need this annotation
//@RestrictsSuspension
class MyStateBuilder<S, A> : ProgramBuilder<MyState<S, A>, A>({
        result -> MyState.Finished(result)
}) {
    suspend fun get(): S = perform { s -> MyState.Get(s) }

    suspend fun put(new: S): Unit = performUnit { s ->
        MyState.Put(new, s) }

}
