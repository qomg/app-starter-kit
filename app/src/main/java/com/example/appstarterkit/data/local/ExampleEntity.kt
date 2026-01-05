package com.example.appstarterkit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "example_table")
data class ExampleEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val timestamp: Long
)
