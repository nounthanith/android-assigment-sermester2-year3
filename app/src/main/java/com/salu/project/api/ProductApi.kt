package com.salu.project.api

import com.salu.project.model.Category
import com.salu.project.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getProducts(@Query("search") search: String? = null): List<Product>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("categories/{id}/products")
    suspend fun getProductsByCategory(@Path("id") categoryId: String): List<Product>
}
