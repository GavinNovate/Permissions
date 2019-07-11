package net.novate.demo.permissions;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import net.novate.permissions.Permissions;

import java.util.Arrays;

public class JavaActivity extends BaseJavaActivity {

    private static final String TAG = "JavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        if (Permissions.hasPermissions(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "hasPermissions: READ_EXTERNAL_STORAGE & WRITE_EXTERNAL_STORAGE");
        }

        Permissions.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                (permissions, results) -> {
                    boolean granted = true;
                    for (int i = 0; i < permissions.length; i++) {
                        granted &= results[i] == Permissions.GRANTED;
                        Log.d(TAG, "requestPermissions: " + permissions[i] + ":" + results[i]);
                    }
                    if (granted) {
                        Log.d(TAG, "requestPermissions: Has all permissions: " + Arrays.toString(permissions));
                    }
                });

        Permissions.requestAllManifestPermissions(this, (permissions, results) -> {
            boolean granted = true;
            for (int i = 0; i < permissions.length; i++) {
                granted &= results[i] == Permissions.GRANTED;
                Log.d(TAG, "requestAllManifestPermissions: " + permissions[i] + ":" + results[i]);
            }
            if (granted) {
                Log.d(TAG, "requestAllManifestPermissions: Has all permissions: " + Arrays.toString(permissions));
            }
        });
    }
}
