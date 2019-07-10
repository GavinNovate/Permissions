package net.novate.demo.permissions;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import net.novate.permissions.Permissions;
import org.jetbrains.annotations.NotNull;


public class JavaFragment extends Fragment {


    public JavaFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_java, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Permissions.requestPermissions(this, new String[]{}, (permissions, results) -> {

        });

        Permissions.requestAllManifestPermissions(this, (permissions, results) -> {

        });
    }
}
