package com.example.kin.menupicture.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.kin.menupicture.fragments.AboutFragment;
import com.example.kin.menupicture.fragments.CameraFragment;
import com.example.kin.menupicture.fragments.GalleryFragment;
import com.example.kin.menupicture.R;
import com.example.kin.menupicture.fragments.SearchFragment;


public class Main_Activity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

//        getSupportFragmentManager();
//
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_camera:
                        selectedFragment = new CameraFragment();
                        break;
                    case R.id.nav_photoGallery:
                        selectedFragment = new GalleryFragment();
                        break;
                    case R.id.nav_about:
                        selectedFragment = new AboutFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            }
    };
}

