package com.petitcl.domain4k.context

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface FinalizingContext {
    fun finalize() = Unit
}

fun finalize(vararg context: Any?) = context.forEach { if (it is FinalizingContext) it.finalize() }

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, R> within(a: A, block: context(A) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val r = block(a)

    finalize(a)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, R> within(a: A, b: B, block: context(A, B) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val r = block(a, b)

    finalize(a)
    finalize(b)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, C, R> within(a: A, b: B, c: C, block: context(A, B, C) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val r = block(a, b, c)

    finalize(a)
    finalize(b)
    finalize(c)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, C, D, R> within(a: A, b: B, c: C, d: D, block: context(A, B, C, D) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val r = block(a, b, c, d)

    finalize(a)
    finalize(b)
    finalize(c)
    finalize(d)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, C, D, E, R> within(a: A, b: B, c: C, d: D, e: E, block: context(A, B, C, D, E) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val r = block(a, b, c, d, e)

    finalize(a)
    finalize(b)
    finalize(c)
    finalize(d)
    finalize(e)
    return r
}

@OptIn(ExperimentalContracts::class)
@Suppress("SUBTYPING_BETWEEN_CONTEXT_RECEIVERS")
inline fun <A, B, C, D, E, F, R> within(a: A, b: B, c: C, d: D, e: E, f: F, block: context(A, B, C, D, E, F) () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val r = block(a, b, c, d, e, f)

    finalize(a)
    finalize(b)
    finalize(c)
    finalize(d)
    finalize(e)
    finalize(f)
    return r
}
