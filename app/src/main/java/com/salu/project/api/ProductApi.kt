package com.salu.project.api

import com.salu.project.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("categories/{id}/products")
    suspend fun getProductsByCategory(@Path("id") categoryId: String): List<Product>
}
