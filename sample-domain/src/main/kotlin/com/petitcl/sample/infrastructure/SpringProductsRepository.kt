package com.petitcl.sample.infrastructure

import com.petitcl.sample.domain.Product
import com.petitcl.sample.domain.ProductSku
import com.petitcl.sample.domain.ProductsRepository
import com.petitcl.sample.domain.toMoney
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Instant

@Repository
internal class SpringProductsRepository(private val repository: SpringJpaRepository) : ProductsRepository {
    override fun save(product: Product): Product
        = product.toEntity()
        .let { repository.save(it) }
        .toDomain()
        .also { println("SpringProductsRepository.save($product)") }

    override fun findBySku(sku: ProductSku): Product? {
        TODO("Not yet implemented")
    }
}

internal interface SpringJpaRepository : JpaRepository<ProductEntity, Long> {
    fun findBySku(sku: String): ProductEntity?
}

@Entity
@Table(name = "products")
internal data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkId")
    val pkId: Long? = null,

    @CreationTimestamp
    @Column(name = "creation_date_time")
    val creationDateTime: Instant?,

    @UpdateTimestamp
    @Column(name = "update_date_time")
    val updateDateTime: Instant?,

    @Column(name = "id")
    val id: String,

    @Column(name = "sku")
    val sku: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "price_amount") val amount: BigDecimal,
    @Column(name = "price_currency") val currency: String,
)

private fun ProductEntity.toDomain() =
    Product(
        sku = ProductSku(this.sku),
        name = this.name,
        description = this.description,
        price = this.amount.toMoney(this.currency),
        attributes = emptyList()
    )

private fun Product.toEntity() =
    ProductEntity(
        id = this.id.value,
        sku = this.sku.value,
        name = this.name,
        description = this.description,
        amount = this.price.number.numberValueExact(BigDecimal::class.java),
        currency = this.price.currency.currencyCode,
        creationDateTime = null,
        updateDateTime = null,
    )
