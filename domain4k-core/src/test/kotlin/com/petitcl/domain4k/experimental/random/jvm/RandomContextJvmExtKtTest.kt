package com.petitcl.domain4k.experimental.random.jvm

import com.petitcl.domain4k.experimental.random.withRandom
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class RandomContextJvmExtKtTest : ShouldSpec({
    context("randomUUID") {
        should("should generate random UUIDs") {
            val results = List(100) { withRandom { randomUUID() } }

            results.distinct().size shouldBe 100
        }
    }
})