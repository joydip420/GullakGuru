package com.example.gullakguru.networks;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class CustomLoggingInterceptor implements Interceptor {

    private void logging(String log) {
        Log.d("SheetData", log);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Log request details
        logging("Request: " + request.method() + " " + request.url());

        for (String headerName : request.headers().names()) {
            logging(headerName);
        }
        logging(new Gson().toJson(request.body()));
        Response response = chain.proceed(request);
        logging("Response: " + response.code() + " " + response.message());

        return response;
    }


}

