package com.example.gullakguru.callbacks;

import com.example.gullakguru.models.AppendResponse;

public interface AppendCallback {

    void onAppendSuccess(Boolean status, AppendResponse response);

    void onAppendFailure(String errorMessage);

}
