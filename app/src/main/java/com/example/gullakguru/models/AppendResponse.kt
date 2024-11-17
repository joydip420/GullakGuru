package com.example.gullakguru.models

data class AppendResponse(
    val spreadsheetId: String,
    val tableRange: String,
    val updates: Updates
)

data class Updates(
    val spreadsheetId: String,
    val updatedRange: String,
    val updatedRows: Int,
    val updatedColumns: Int,
    val updatedCells: Int
)
