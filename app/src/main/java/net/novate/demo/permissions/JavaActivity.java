package net.novate.demo.permissions;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import net.novate.permissions.Permissions;


public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        Permissions.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, (permissions, results) -> {
            for (int result : results) {
                if (result == Permissions.GRANTED) {

                }
            }
        });

        Permissions.requestAllManifestPermissions(this, (permissions, results) -> {

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permissions.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
