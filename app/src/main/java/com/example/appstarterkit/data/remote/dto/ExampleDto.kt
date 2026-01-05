package com.example.appstarterkit.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExampleDto(
    val id: String,
    val name: String,
    val description: String?,
    val createdAt: Long
)
