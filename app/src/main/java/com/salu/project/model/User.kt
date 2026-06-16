package com.salu.project.model

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val password: String? = null,
    val role: String? = null,
    val createdAt: String? = null,
    val token: String? = null
)
