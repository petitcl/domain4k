package com.petitcl.sample.domain

import arrow.core.continuations.either
import com.petitcl.domain4k.context.EventsContext
import com.petitcl.domain4k.stereotype.DomainEvent
import com.petitcl.domain4k.stereotype.ErrorOr
import javax.money.MonetaryAmount

context(EventsContext)
suspend fun Product.Companion.newProduct(
    sku: ProductSku,
    name: String,
    description: String,
    price: MonetaryAmount,
    attributes: List<ProductAttribute> = emptyList(),
): ErrorOr<Product> = either {
    validateAttributes(attributes).bind()
    Product(sku, name, description, price, attributes)
        .also {
            publishEvent(ProductCreatedEvent(sku, name, description, price, attributes))
        }
}

data class ProductCreatedEvent(
    val sku: ProductSku,
    val name: String,
    val description: String,
    val price: MonetaryAmount,
    val attributes: List<ProductAttribute>,
) : DomainEvent
