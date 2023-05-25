package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent


data class Ev(val value: String) : DomainEvent

fun fn1() = "Result1" to listOf(Ev("1"), Ev("2"), Ev("3"))
fun fn2(r: String) = "$r - Result2" to listOf(Ev("4"), Ev("5"), Ev("6"))
fun fn3(r: String) = "$r - Result3" to listOf(Ev("7"), Ev("8"), Ev("9"))
fun fn4(r: String) = "$r - Result4" to emptyList<DomainEvent>()
fun fn5(r: String) = "$r - Result5"

fun main() {
    val computation: MyEventMonad<String> = MyEventMonad.create()
        .flatMap { fn1() }
        .flatMap { fn2(it) }
        .flatMap { fn3(it) }
        .flatMap { fn4(it) }
        .map { fn5(it) }
        .addEvents { listOf(Ev("10")) }

    val (result, emittedValues) = computation.run()

    println("Emitted values: $emittedValues")
    println("Result: $result")
}

fun main3() {
    val computation: MyMonad<String, Int> =
        State.set<List<Int>>(emptyList())
            .flatMap { emitAndReturn("Result1", 1, 2, 3) }
            .flatMap { emitAndReturn("$it - Result2", 4, 5, 6) }
            .flatMap { emitAndReturn("$it - Result3", 7, 8, 9) }
            .flatMap { emitAndReturn("$it - Result4") }
            .map { "$it - Result5" }

    val (result, emittedValues) = computation.run(emptyList())

    println("Emitted values: $emittedValues")
    println("Result: $result")
}

