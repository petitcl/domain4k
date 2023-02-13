package com.petitcl.sample.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductsControllerIntTest {

    @Autowired private lateinit var mockMvc: MockMvc

    @Test
    fun `create product v1`() {
        mockMvc.post("/v1/products/v1") {
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `create product v2`() {
        mockMvc.post("/v1/products/v2") {
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `create product v3`() {
        mockMvc.post("/v1/products/v3") {
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `create product v4`() {
        mockMvc.post("/v1/products/v4") {
        }.andExpect {
            status { isOk() }
        }
    }
}