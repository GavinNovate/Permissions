package net.novate.demo.permissions;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.novate.permissions.Permissions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class JavaFragment extends Fragment {

    private static final String TAG = "JavaFragment";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_java, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
