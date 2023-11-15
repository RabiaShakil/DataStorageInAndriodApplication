package com.example.assignment3

import androidx.room.Entity
import androidx.room.PrimaryKey

// Contact.kt
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNo: String,
    // Add other fields as needed
)
