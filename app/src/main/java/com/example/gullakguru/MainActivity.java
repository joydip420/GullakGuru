package com.example.gullakguru;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gullakguru.callbacks.AppendCallback;
import com.example.gullakguru.callbacks.ExpenseCallback;
import com.example.gullakguru.controllers.NetworkController;
import com.example.gullakguru.databinding.ActivityMainBinding;
import com.example.gullakguru.models.AppendResponse;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpSpinner();

        loadData();

        binding.buttonUpload.setOnClickListener(view -> {
            Log.d("SheetData", "Button clicked");
            sendData();
        });
    }

    private void sendData() {
        String amount = binding.editTextAmount.getText().toString();
        String description = binding.editTextDescription.getText().toString();

        String category = binding.spinner.getSelectedItem().toString();


        NetworkController controller = new NetworkController();
        controller.sendData(this, amount, category, description, new AppendCallback() {
            @Override
            public void onAppendSuccess(Boolean status, AppendResponse response) {
                    if (status) {
                        binding.editTextAmount.setText("");
                        binding.editTextDescription.setText("");
                        binding.spinner.setSelection(0);
                        vibratePhone(MainActivity.this);
                        loadData();
                        Toast.makeText(MainActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onAppendFailure(String errorMessage) {

            }
        });

    }





    private void setUpSpinner() {
        Spinner spinner = binding.spinner;

        String[] items = {"Travel", "Food and Dining","Shopping","Education","Rent","Health care","Bills","Insurance","Transport","Others"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,  // Custom layout for dropdown items
                items
        );
        spinner.setAdapter(adapter);
    }

    private void loadData() {
        NetworkController controller = new NetworkController();

        controller.fetchSheetData(this,
                "dashboard!A:B",
                (todayExpense, monthlyExpense) -> {
                    binding.textViewDailyExpenses.setText("Daily Expenses: ₹"+todayExpense);
                    binding.textViewMonthlyExpenses.setText("Monthly Expenses: ₹"+monthlyExpense);
                });

    }
    public void vibratePhone(Context context) {
        // Get the Vibrator service
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
        }
    }


}