package com.petitcl.domain4k.experimental.random.jvm

import com.petitcl.domain4k.experimental.random.withRandom
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName

class RandomContextJvmExtKtTest : ShouldSpec({
    context("randomUUID") {
        should("should generate random UUIDs by default") {
            val results = withRandom { List(100) { randomUUID() } }

            results.distinct().size shouldBe 100
        }

        should("should generate same UUIDs when seeded") {
            val results = withRandom(seed = 4242) { List(10) { randomUUID().toString() } }

            results shouldBe listOf(
                "4b6b096f-8b10-393c-86fe-cbff16f76308",
                "f4f6349d-2e14-3b63-a659-504bf6d1d3d7",
                "9e8490d8-970a-3dcc-b326-194e1d7c13b3",
                "50f1c64b-1c00-319e-a7d7-4996187d9ebc",
                "4ce6784a-8ec7-3b6e-8d3d-d89924ac784d",
                "53519634-75c0-3ac6-a0cc-601194d688d0",
                "d03979db-8ad4-3e1e-b979-94c70f8bc275",
                "6eb55b58-3d0f-3630-8726-f9763821b6f1",
                "579f8f6c-0277-318e-8150-2c6915b1b22a",
                "b5194877-4b58-30f4-ae4c-f3df30b80be8"
            )
            println(results)
        }
    }
})
