package com.example.retrocoroutines.models

import androidx.annotation.Keep

@Keep
data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
)
