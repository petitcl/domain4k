package com.petitcl.domain4k.stereotype

import arrow.core.Either

typealias ErrorOr<T> = Either<AppError, T>

/**
 * Base interface for errors
 */
interface AppError {
    val message: String
    val cause: Throwable?
}
