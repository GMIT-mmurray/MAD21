package com.ebookfrenzy.androidsample;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.ebookfrenzy.androidsample.databinding.ActivityMainBinding;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void convertCurrency(View view) {

        if (!binding.dollarText.getText().toString().equals("")) {

            float dollarValue = Float.valueOf(binding.dollarText.getText().toString());
            Float euroValue = dollarValue * 0.85F;
            binding.textView.setText(euroValue.toString());
        } else {
            binding.textView.setText(R.string.no_value_string);
        }
    }
}