package com.ebookfrenzy.dynamicfeature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

import java.util.Collections;
import java.util.Locale;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private SplitInstallManager manager;
    private int mySessionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.status_text);
        manager = SplitInstallManagerFactory.create(this);
    }

    public void launchFeature(View view) {
        if (manager.getInstalledModules().contains("my_dynamic_feature")) {
            Intent i = new Intent(
                    "com.ebookfrenzy.my_dynamic_feature.MyFeatureActivity");
            startActivity(i);
        } else {
            statusText.setText("Feature not yet installed");
        }
    }

    public void installFeature(View view) {

        SplitInstallRequest request =
                SplitInstallRequest
                        .newBuilder()
                        .addModule("my_dynamic_feature")
                        .build();

        manager.registerListener(listener);

        manager.startInstall(request)
                .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer sessionId) {
                        mySessionId = sessionId;
                        Toast.makeText(MainActivity.this,
                                "Module installation started",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(MainActivity.this,
                                "Module installation failed" + exception.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteFeature(View view) {
        manager.deferredUninstall(Collections.singletonList("my_dynamic_feature"));
    }

    SplitInstallStateUpdatedListener listener =
            new SplitInstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(SplitInstallSessionState state) {

                    if (state.sessionId() == mySessionId) {
                        switch (state.status()) {
                            case SplitInstallSessionStatus.DOWNLOADING:
                                long size = state.totalBytesToDownload();
                                long downloaded = state.bytesDownloaded();
                                statusText.setText(String.format(Locale.getDefault(),
                                        "%d of %d bytes downloaded.", downloaded, size));
                                break;

                            case SplitInstallSessionStatus.INSTALLING:

                                statusText.setText("Installing feature");
                                break;

                            case SplitInstallSessionStatus.DOWNLOADED:

                                statusText.setText("Download Complete");
                                break;

                            case SplitInstallSessionStatus.INSTALLED:

                                statusText.setText("Installed - Feature is ready");
                                break;

                            case SplitInstallSessionStatus.CANCELED:

                                statusText.setText("Installation cancelled");
                                break;

                            case SplitInstallSessionStatus.PENDING:

                                statusText.setText("Installation pending");
                                break;

                            case SplitInstallSessionStatus.FAILED:

                                statusText.setText("Installation Failed. Error code: " +
                                        state.errorCode());
                        }
                    }
                }
            };

    @Override
    public void onResume() {
        manager.registerListener(listener);
        super.onResume();
    }

    @Override
    public void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }

}