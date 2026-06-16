package com.salu.project.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val category: String,
    val stock: Int,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)
