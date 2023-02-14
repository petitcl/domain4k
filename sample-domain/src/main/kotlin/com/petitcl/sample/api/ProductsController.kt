package com.petitcl.sample.api

import com.petitcl.sample.application.CreateProductCommand
import com.petitcl.sample.application.ProductsService
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

val command = CreateProductCommand(
    sku = "ABCDEF123456",
    name = "Product 123",
    description = "Product 123 description",
    price = 123.0,
    currency = "EUR",
)

@RestController
@RequestMapping("/v1/products")
class ProductsController(
    private val productsService: ProductsService,
) {

    @PostMapping
    fun createProducts() = runBlocking {
        productsService.createProduct(command)
    }

}