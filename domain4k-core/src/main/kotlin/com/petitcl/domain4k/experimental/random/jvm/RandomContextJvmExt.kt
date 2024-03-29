package com.petitcl.domain4k.experimental.random.jvm

import com.petitcl.domain4k.experimental.random.RandomContext
import com.petitcl.domain4k.experimental.random.RandomDsl
import com.petitcl.domain4k.experimental.random.withRandom
import java.util.UUID

context(RandomContext)
@RandomDsl
fun randomUUID(): UUID {
    val bytes = nextBytes(16)
    return UUID.nameUUIDFromBytes(bytes)
}

fun main() {
    withRandom(seed = 1234) {
        println(randomUUID())
        println(randomUUID())
        println(randomUUID())
        println(randomUUID())
    }

}