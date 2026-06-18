package com.salu.project.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("stock")
    val stock: Int?,
    @SerializedName("isActive")
    val isActive: Boolean?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)
