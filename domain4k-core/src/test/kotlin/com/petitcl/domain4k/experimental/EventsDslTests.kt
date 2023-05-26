package com.petitcl.domain4k.experimental

import com.petitcl.domain4k.stereotype.DomainEvent
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


context(EventsContext)
private suspend fun fn1(): String {
    publish(Ev("1"), Ev("2"), Ev("3"))
    return "Result1"
}

context(EventsContext)
private suspend fun fn2(r: String)  : String {
    publish(Ev("4"), Ev("5"), Ev("6"))
    return "$r - Result2"
}

context(EventsContext)
private suspend fun fn3(r: String) : String {
    publish(Ev("7"), Ev("8"), Ev("9"))
    return "$r - Result3"
}

context(EventsContext)
private suspend fun fn4(r: String) : String {
    publish(Ev("10"))
    return "$r - Result4"
}

context(EventsContext)
private fun fn5(r: String) : String = "$r - Result5"

private fun IntRange.toEvents(): List<DomainEvent> = map { Ev(it.toString()) }
private const val expectedResult = "Result1 - Result2 - Result3 - Result4 - Result5"

class EventsDslTests : ShouldSpec({

    context("events") {
        should("should accumulate events") {
            val (events, result) = events {
                val r1 = fn1()
                val r2 = fn2(r1)
                val r3 = fn3(r2)
                val r4 = fn4(r3)
                fn5(r4)
            }

            events shouldBe (1..10).toEvents()
            result shouldBe expectedResult
        }
    }

    context("events with lazy collector") {
        should("should accumulate events") {
            val collector = EventsCollectors.inMemory()
            val result: String = events(collector) {
                val r1 = fn1()
                collector.events shouldBe emptyList()
                val r2 = fn2(r1)
                collector.events shouldBe emptyList()
                val r3 = fn3(r2)
                collector.events shouldBe emptyList()
                val r4 = fn4(r3)
                collector.events shouldBe emptyList()
                val r5 = fn5(r4)
                collector.events shouldBe emptyList()
                r5
            }

            collector.events shouldBe (1..10).toEvents()
            result shouldBe expectedResult
        }
    }

    context("events with eager collector") {
        should("should accumulate events") {
            val collector = EventsCollectors.inMemory()
            val result: String = eventsEager(collector) {
                val r1 = fn1()
                collector.events shouldBe (1..3).toEvents()
                val r2 = fn2(r1)
                collector.events shouldBe (1..6).toEvents()
                val r3 = fn3(r2)
                collector.events shouldBe (1..9).toEvents()
                val r4 = fn4(r3)
                collector.events shouldBe (1..10).toEvents()
                val r5 = fn5(r4)
                collector.events shouldBe (1..10).toEvents()
                r5
            }

            collector.events shouldBe (1..10).toEvents()
            result shouldBe expectedResult
        }
    }
})
