package com.petitcl.sample.application

import arrow.core.continuations.either
import com.petitcl.domain4k.context.EventsContext
import com.petitcl.domain4k.context.lazy
import com.petitcl.domain4k.context.within
import com.petitcl.domain4k.stereotype.ErrorOr
import com.petitcl.sample.domain.*
import org.springframework.stereotype.Service

data class CreateProductCommand(
    val sku: String,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
)

@Service
class ProductsService(
    private val repository: ProductsRepository,
    private val events: EventsContext,
) {

    suspend fun createProduct(request: CreateProductCommand): ErrorOr<Product> = either {
        within(events.lazy()) {
            val product = Product.newProduct(
                sku = ProductSku(request.sku),
                name = request.name,
                description = request.description,
                price = request.price.toMoney(request.currency),
            ).bind()
            repository.save(product)
        }
    }
}
