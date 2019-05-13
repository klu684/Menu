package com.example.kin.menupicture.Contact;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.example.kin.menupicture.R;
import com.example.kin.menupicture.helpers.BottomNavigationViewHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        nMap = googleMap;

        moveCamera(new LatLng(35.6916972332, 139.769746921),
                15f,
                "SmartMenu Inc.");

        nMap.setBuildingsEnabled(true);
        nMap.setIndoorEnabled(true);
    }

    private static final String TAG = "ContactActivity";
    private static final int ACTIVITY_NUM = 3; //this is 3 until search button comes back it will be 4 again
    private Context mContext = ContactActivity.this;
    private GoogleMap nMap;
    private String link, email, facebook;
    private Intent intent;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Log.d(TAG, "onCreate: starting.");

        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.btn_linkedln);
        CircleImageView circleImageView2 = (CircleImageView) findViewById(R.id.btn_email);
        CircleImageView circleImageView3 = (CircleImageView) findViewById(R.id.btn_facebook);

        circleImageView.setOnClickListener(this);
        circleImageView2.setOnClickListener(this);
        circleImageView3.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(ContactActivity.this);

        setupBottomNavigationView();
        setupToolbar();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_linkedln:
                v.startAnimation(buttonClick);
                link = "https://www.linkedin.com/m/login/";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                startActivity(intent);
                break;
            case R.id.btn_email:
                v.startAnimation(buttonClick);
                email = "https://www.google.com/gmail/about/#";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(email));
                startActivity(intent);
                break;
            case R.id.btn_facebook:
                v.startAnimation(buttonClick);
                facebook = "https://www.facebook.com/";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebook));
                startActivity(intent);
                break;

        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        nMap.addMarker(options);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "onCLick: navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setCheckable(true);
    }
}