package com.petitcl.domain4k.experimental.random

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe

class RandomDslTests : ShouldSpec({
    context("nextInt") {
        should("should generate random values") {
            val results = withRandom { List(100) { nextInt() } }

            results.forEach {
                it shouldBeGreaterThanOrEqual Int.MIN_VALUE
                it shouldBeLessThanOrEqual Int.MAX_VALUE
            }
            // pseudo check for randomness - this might bite me in the ass in the future
            results.distinct().size shouldBeGreaterThan 50
        }
    }

    context("nextLong") {
        should("should generate random values") {
            val results = withRandom { List(100) { nextLong() } }

            results.forEach {
                it shouldBeGreaterThanOrEqual Long.MIN_VALUE
                it shouldBeLessThanOrEqual Long.MAX_VALUE
            }
            results.distinct().size shouldBeGreaterThan 50
        }
    }

    context("nextBoolean") {
        should("should generate random values") {
            val results = withRandom { List(100) { nextBoolean() } }

            results.forEach {
                it shouldBeIn listOf(false, true)
            }
            results.distinct().size shouldBe 2
        }
    }

    context("random") {
        should("should generate using Kotlin's Default random by default") {
            val results = withRandom { List(100) {nextInt().toString() } }

            results.distinct().size shouldBeGreaterThan 50
        }

        should("should always generate same result if we pass a seed") {
            val results = withRandom(seed = 424242) {
                List(10) { nextInt(100).toString() }
            }

            results shouldBe listOf("45", "95", "64", "92", "73", "7", "34", "58", "97", "4")
        }
    }
})