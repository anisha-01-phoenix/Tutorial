package com.example.retrocoroutines.models

import androidx.annotation.Keep

@Keep
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val company:Company,
    val website: String
)

