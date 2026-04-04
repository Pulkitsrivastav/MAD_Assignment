package com.example.currencyconverterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText amountInput;
    Spinner fromCurrency, toCurrency;
    TextView resultText;
    Button convertBtn, settingsBtn;

    String[] currencies = {"INR", "USD", "EUR", "JPY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountInput = findViewById(R.id.amountInput);
        fromCurrency = findViewById(R.id.fromCurrency);
        toCurrency = findViewById(R.id.toCurrency);
        resultText = findViewById(R.id.resultText);
        convertBtn = findViewById(R.id.convertBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        // Set spinner data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, currencies);

        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);

        // Convert button
        convertBtn.setOnClickListener(v -> convertCurrency());

        // Settings button
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void convertCurrency() {

        String amountStr = amountInput.getText().toString();

        if (amountStr.isEmpty()) {
            resultText.setText("Enter amount");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        String from = fromCurrency.getSelectedItem().toString();
        String to = toCurrency.getSelectedItem().toString();

        double result = convert(from, to, amount);

        resultText.setText("Converted: " + result);
    }

    private double convert(String from, String to, double amount) {

        double inr = 0;

        // Convert to INR first
        switch (from) {
            case "INR": inr = amount; break;
            case "USD": inr = amount * 83; break;
            case "EUR": inr = amount * 90; break;
            case "JPY": inr = amount * 0.55; break;
        }

        // Convert INR to target
        switch (to) {
            case "INR": return inr;
            case "USD": return inr / 83;
            case "EUR": return inr / 90;
            case "JPY": return inr / 0.55;
        }

        return 0;
    }
}