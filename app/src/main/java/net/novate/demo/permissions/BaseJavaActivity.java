package net.novate.demo.permissions;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import net.novate.permissions.Permissions;

@SuppressLint("Registered")
public class BaseJavaActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permissions.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
