package com.petitcl.domain4k.experimental.random

import kotlin.random.Random

fun <A> withRandom(
    context: RandomContext = RandomContexts.Default,
    block: RandomContext.() -> A
): A {
    return context.block()
}

fun <A> withRandom(
    seed: Long? = null,
    secure: Boolean = false,
    block: RandomContext.() -> A
): A {
    // TODO: do something with secure
    val context = if (seed != null) RandomContexts.seeded(seed) else RandomContexts.Default
    return withRandom(
        context = context,
        block = block
    )
}

class KotlinRandomContext(private val random: Random) : RandomContext {
    override fun nextBits(bitCount: Int): Int = random.nextBits(bitCount)

    override fun nextInt(): Int = random.nextInt()

    override fun nextInt(until: Int): Int = random.nextInt(until)

    override fun nextInt(from: Int, until: Int): Int = random.nextInt(from, until)

    override fun nextLong(): Long = random.nextLong()

    override fun nextLong(until: Long): Long = random.nextLong(until)

    override fun nextLong(from: Long, until: Long): Long  = random.nextLong(from, until)

    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextDouble(): Double = random.nextDouble()

    override fun nextDouble(until: Double): Double = random.nextDouble(until)

    override fun nextDouble(from: Double, until: Double): Double = random.nextDouble(from, until)

    override fun nextFloat(): Float = random.nextFloat()

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray = random.nextBytes(array, fromIndex, toIndex)

    override fun nextBytes(array: ByteArray): ByteArray = random.nextBytes(array)

    override fun nextBytes(size: Int): ByteArray = random.nextBytes(size)

}