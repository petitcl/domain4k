package com.petitcl.domain4k.experimental.random


import kotlin.random.Random

@DslMarker
annotation class RandomDsl

@RandomDsl
interface RandomContext {

    @RandomDsl
    fun nextBits(bitCount: Int): Int

    @RandomDsl
    fun nextInt(): Int

    @RandomDsl
    fun nextInt(until: Int): Int

    @RandomDsl
    fun nextInt(from: Int, until: Int): Int

    @RandomDsl
    fun nextLong(): Long

    @RandomDsl
    fun nextLong(until: Long): Long

    @RandomDsl
    fun nextLong(from: Long, until: Long): Long

    @RandomDsl
    fun nextBoolean(): Boolean

    @RandomDsl
    fun nextDouble(): Double

    @RandomDsl
    fun nextDouble(until: Double): Double

    @RandomDsl
    fun nextDouble(from: Double, until: Double): Double

    @RandomDsl
    fun nextFloat(): Float

    @RandomDsl
    fun nextBytes(array: ByteArray, fromIndex: Int = 0, toIndex: Int = array.size): ByteArray

    @RandomDsl
    fun nextBytes(array: ByteArray): ByteArray

    @RandomDsl
    fun nextBytes(size: Int): ByteArray

}

// TODO: Add multi platform secure random
object RandomContexts {
    val Default: RandomContext = KotlinRandomContext(Random.Default)

    fun of(random: Random) = KotlinRandomContext(random)

    fun seeded(seed: Int) = KotlinRandomContext(Random(seed))

    fun seeded(seed: Long) = KotlinRandomContext(Random(seed))

}

fun Random.asContext(): RandomContext = KotlinRandomContext(this)
