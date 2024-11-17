package com.example.gullakguru.controllers;

import android.content.Context;
import android.util.Log;

import com.example.gullakguru.callbacks.AppendCallback;
import com.example.gullakguru.callbacks.ExpenseCallback;
import com.example.gullakguru.models.AppendResponse;
import com.example.gullakguru.models.SheetResponse;
import com.example.gullakguru.networks.Retrofit;
import com.example.gullakguru.networks.RetrofitController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkController {

    String spreadSheetId = "1DtQK2bltJZo2LaVMF1O0UxHUscXpU6tGRqGR8R2fSNA";
    String apiKey = "AIzaSyATehklrIptuU5MCMikWwRbudB937VMYbo";

    public static String accessToken = "ya29.a0AeDClZA_xRXAN1ww3TRv4nKkBInLJQ_ZxBCBW5lv8GsFE9QCN26hJTSxOdPv6tLkh2ZB0SQ-5kdqDyhp6OCLDaiOUnUUFnIgb_rZKkPlzB_CS3vo4IJBq5UMb4iDY1dRgBiYyd9RWt1gCeoB8YPN-SUpCOV77NMHglCOG_yxaCgYKARQSARASFQHGX2MioDAEa3m1fVHPdbeR_I-7zg0175";

    public void sendData(Context context, String amount, String category, String description, AppendCallback callback) {
        String range = "transaction_db!A:D";

        RetrofitController service = Retrofit.getRetrofit(context).create(RetrofitController.class);

        // Prepare data
        JsonArray jsonArray = new JsonArray(4);
        jsonArray.add(getTodayDate());
        jsonArray.add(category);
        jsonArray.add(description);
        jsonArray.add(Integer.parseInt(amount));

        JsonArray array = new JsonArray(1);
        array.add(jsonArray);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("values", array);

        Call<AppendResponse> call = service.appendData(
                spreadSheetId,
                range, // Range to append data
                "RAW",
                "Bearer " +accessToken,
                jsonObject
        );

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AppendResponse> call, Response<AppendResponse> response) {
                if (response.isSuccessful()) {
                    callback.onAppendSuccess(true, response.body());
                    Log.d("GoogleSheets", "Expense added successfully!");
                } else {
                    callback.onAppendFailure(response.errorBody().toString());
                    Log.e("GoogleSheets", "Failed to add expense: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AppendResponse> call, Throwable t) {
                Log.e("GoogleSheets", "API call failed", t);
            }
        });

    }

    private String getTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return today.format(formatter);
    }


    public void fetchSheetData(Context context, String range, ExpenseCallback callback) {
        RetrofitController service = Retrofit.getRetrofit(context).create(RetrofitController.class);

        Call<SheetResponse> call = service.getSheetData(spreadSheetId, range, apiKey);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<SheetResponse> call, Response<SheetResponse> response) {
                if (response.isSuccessful()) {
                    List<List<String>> data = response.body().getValues();
                    String todayExpense = "", monthlyExpense = "";
                    for (List<String> row : data) {
                        Log.d("SheetData", "Row: " + row.toString());
                        if (row.get(0).equalsIgnoreCase("Today expense")) {
                            todayExpense = row.get(1);
                        }
                        if (row.get(0).equalsIgnoreCase("Monthly Expense")) {
                            monthlyExpense = row.get(1);
                        }
                    }
                    callback.fetchedData(todayExpense, monthlyExpense);
                } else {
                    Log.e("SheetData", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<SheetResponse> call, Throwable t) {
                Log.e("SheetData", "Failed to fetch data", t);
            }
        });
    }

}
