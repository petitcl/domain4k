package com.petitcl.domain4k.experimental

import fp.serrano.inikio.program

fun <S, A> state(block: suspend MyStateBuilder<S, A>.() -> A): MyState<S, A> =
    program(MyStateBuilder(), block)

fun increment(): MyState<Int, String> = state {
    put(get() + 1)
    helloFromScope()
}

context(MyStateBuilder<Int, String>)
        suspend fun helloFromScope() : String {
    return "Hello from scope ${get()}"
}

fun main() {
    val stateExample = increment().execute(0)
    println(stateExample)
}
