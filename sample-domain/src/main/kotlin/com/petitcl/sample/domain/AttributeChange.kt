package com.petitcl.sample.domain

import arrow.core.continuations.EffectScope
import com.petitcl.domain4k.context.EventsContext
import com.petitcl.domain4k.stereotype.AppError
import com.petitcl.domain4k.stereotype.DomainEvent


context(EffectScope<AppError>, EventsContext)
suspend fun Product.addAttribute(
    attribute: ProductAttribute,
): Product = this.copy(attributes = validateAttributes(attributes + attribute).bind())
    .also { publishEvent(AttributesAddedToProductEvent(sku, listOf(attribute))) }

context(EffectScope<AppError>, EventsContext)
suspend fun Product.addAttributes(
    attributes: List<ProductAttribute>,
): Product = this.copy(attributes = validateAttributes(attributes + attributes).bind())
    .also { publishEvent(AttributesAddedToProductEvent(sku, attributes)) }

context(EventsContext)
fun Product.removeAttribute(
    attribute: ProductAttribute,
): Product = this.copy(attributes = attributes.filter { it.name != attribute.name })
    .also { publishEvent(AttributesRemovedFromProductEvent(sku, listOf(attribute))) }

context(EventsContext)
fun Product.removeAttributes(
    attributes: List<ProductAttribute>,
): Product = this.copy(attributes = attributes.filter { attributes.any { a -> a.name == it.name } })
    .also { publishEvent(AttributesRemovedFromProductEvent(sku, attributes)) }

data class AttributeChange(
    val name: String,
    val oldValue: String,
    val newValue: String,
)

data class AttributesAddedToProductEvent(
    val sku: ProductSku,
    val addedAttributes: List<ProductAttribute>,
) : DomainEvent

data class AttributesRemovedFromProductEvent(
    val sku: ProductSku,
    val removedAttributes: List<ProductAttribute>,
) : DomainEvent
