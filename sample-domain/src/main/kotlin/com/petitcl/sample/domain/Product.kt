package com.petitcl.sample.domain

import arrow.core.left
import arrow.core.right
import com.petitcl.domain4k.stereotype.*
import javax.money.MonetaryAmount

@JvmInline
value class ProductSku(val value: String) {
    init {
        require(value.isNotBlank()) { "ProductSku cannot be blank" }
        require(value.contains(Regex("^[A-Z0-9]{12}$")) ) { "ProductSku should have valid format" }
    }
}

/**
 * Attribute names must be unique per Product
 */
data class Product(
    val sku: ProductSku,
    val name: String,
    val description: String,
    val price: MonetaryAmount,
    val attributes: List<ProductAttribute>,
) : DomainEntity<ProductSku, Product> {
    init {
        validateAttributes(attributes)
    }

    override val id = sku

    override fun eq(other: Any?): Boolean = other != null
            && (other is Product)
            && (this.id == other.id)

    companion object
}

data class ProductAttributesNotValid(
    override val message: String,
    override val cause: Throwable? = null
) : AppError

internal fun validateAttributes(attributes: List<ProductAttribute>): ErrorOr<List<ProductAttribute>> {
    val attributeNamesAreUnique = attributes.map { it.name }.toSet().size == attributes.size
    if (!attributeNamesAreUnique) {
        return ProductAttributesNotValid("Attributes must have unique names").left()
    }
    return attributes.right()
}

data class ProductAttribute(
    val name: String,
    val description: String,
    val value: String
)
