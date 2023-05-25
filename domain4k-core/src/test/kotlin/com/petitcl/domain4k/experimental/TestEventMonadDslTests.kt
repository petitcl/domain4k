package com.petitcl.domain4k.experimental


context(EventsScope)
suspend fun fn1(): String {
    emit(Ev("1"), Ev("2"), Ev("3"))
    return "Result1"
}

context(EventsScope)
suspend fun fn2(r: String)  : String {
    emit(Ev("4"), Ev("5"), Ev("6"))
    return "$r - Result2"
}

context(EventsScope)
suspend fun fn3(r: String) : String {
    emit(Ev("7"), Ev("8"), Ev("9"))
    return "$r - Result3"
}

context(EventsScope)
suspend fun fn4(r: String) : String {
    emit(Ev("10"))
    return "$r - Result4"
}

context(EventsScope)
fun fn5(r: String) : String = "$r - Result5"

/**
 * Things to improve:
 * - context(EventsScope<String>) needs to include the return type, which is cumbersome
 * - functions need to be suspend functions
 */
fun main() {
    val (events, result) = events {
        val r1 = fn1()
        val r2 = fn2(r1)
        val r3 = fn3(r2)
        val r4 = fn4(r3)
        fn5(r4)
    }
    println("Events: $events")
    println("Result: $result")
}
