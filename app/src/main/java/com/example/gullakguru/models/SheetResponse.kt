package com.example.gullakguru.models

data class SheetResponse(
    val range: String,
    val majorDimension: String,
    val values: List<List<String>>
)