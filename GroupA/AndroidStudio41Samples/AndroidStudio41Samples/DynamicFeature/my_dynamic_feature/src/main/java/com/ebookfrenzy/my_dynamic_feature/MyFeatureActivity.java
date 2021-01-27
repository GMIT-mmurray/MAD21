package com.ebookfrenzy.my_dynamic_feature;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.play.core.splitcompat.SplitCompat;

import android.os.Bundle;

public class MyFeatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplitCompat.install(this);
        setContentView(R.layout.activity_my_feature);
    }
}