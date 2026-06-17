package com.salu.project.model

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class CartResponse(
    val items: List<CartItem>,
    val totalPrice: Double
)

data class AddToCartRequest(
    val productId: String,
    val price: Double,
    val quantity: Int
)

data class UpdateQuantityRequest(
    val quantity: Int
)
