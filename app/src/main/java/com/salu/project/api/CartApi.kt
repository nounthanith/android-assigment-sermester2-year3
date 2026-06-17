package com.salu.project.api

import com.salu.project.model.AddToCartRequest
import com.salu.project.model.CartResponse
import com.salu.project.model.UpdateQuantityRequest
import retrofit2.http.*

interface CartApi {
    @GET("cart")
    suspend fun getCart(): CartResponse

    @POST("cart")
    suspend fun addToCart(@Body body: AddToCartRequest): CartResponse

    @DELETE("cart")
    suspend fun clearCart(): Map<String, String>

    @PUT("cart/{productId}")
    suspend fun updateQuantity(
        @Path("productId") productId: String,
        @Body body: UpdateQuantityRequest
    ): CartResponse

    @DELETE("cart/{productId}")
    suspend fun removeItem(@Path("productId") productId: String): CartResponse
}
