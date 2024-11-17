package com.example.gullakguru.networks

import com.example.gullakguru.controllers.NetworkController
import com.example.gullakguru.models.AppendResponse
import com.example.gullakguru.models.SheetResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Objects


interface RetrofitController {

    @GET("v4/spreadsheets/{spreadsheetId}/values/{range}")
    fun getSheetData(
        @Path("spreadsheetId") spreadsheetId: String,
        @Path("range") range: String,
        @Query("key") apiKey: String
    ): Call<SheetResponse>

    @POST("v4/spreadsheets/{spreadsheetId}/values/{range}:append")
    @Headers("Content-Type: application/json")
    @JvmSuppressWildcards
    fun appendData(
        @Path("spreadsheetId") spreadsheetId: String?,
        @Path("range") range: String?,
        @Query("valueInputOption") valueInputOption: String?,
        @Header("Authorization") authorization: String?,
        @Body body: JsonObject
    ): Call<AppendResponse?>?

}