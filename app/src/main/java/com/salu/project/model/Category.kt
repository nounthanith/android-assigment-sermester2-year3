package com.salu.project.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("image")
    val image: String? = null
)
