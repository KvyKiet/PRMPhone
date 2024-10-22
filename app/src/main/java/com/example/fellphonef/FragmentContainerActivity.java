package com.example.fellphonef;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FragmentContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment_container);

        if (savedInstanceState == null) {
            // Add the MapsFragment to this activity
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MapsFragment())
                    .commit();
        }
    }
}