package com.example.maledettatreest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.maledettatreest.ui.bacheca.Bacheca;
import com.example.maledettatreest.ui.profile.Profilo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bNavigation = findViewById(R.id.bottom_navigation);

        Bacheca bacheca = Bacheca.newInstance();
        Profilo profilo = Profilo.newInstance();

        openFragment(bacheca);

        bNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.stazioni) {
                openFragment(bacheca);
                return true;
            } else if (itemId == R.id.profilo) {
                openFragment(profilo);
                return true;
            }
            return false;
        });

    }

    private void openFragment(Fragment fragment) {
        Log.d("TabBar", "openFragment: ");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.commit(); // commit() performs the action
    }

}