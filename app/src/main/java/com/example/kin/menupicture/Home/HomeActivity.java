package com.example.kin.menupicture.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.kin.menupicture.Login.LoginActivity;
import com.example.kin.menupicture.R;
import com.example.kin.menupicture.Utils.GridImageAdapter;
import com.example.kin.menupicture.Utils.SectionsPagerAdapter;
import com.example.kin.menupicture.Utils.UniversalImageLoader;
import com.example.kin.menupicture.helpers.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity{

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 0;
    private static final int NUM_GRID_COLUMNS = 3;
    private Context mContext = HomeActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);

        setupFirebaseAuth();
//        initImageLoader();
//        tempGridSetup();
        setupBottomNavigationView();
        setupViewPager();

    }


//    private void initImageLoader(){
//        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(universalImageLoader.getConfig());
//    }

    //not working
//    private void tempGridSetup(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://static01.nyt.com/images/2018/08/29/dining/29BurnerHill1/29BurnerHill1-articleLarge.jpg?quality=75&auto=webp&disable=upscale");
//        imgURLs.add("https://img.huffingtonpost.com/asset/585be1aa1600002400bdf2a6.jpeg?ops=scalefit_970_noupscale");
//        imgURLs.add("https://grist.files.wordpress.com/2017/06/vegetables.jpg?w=1200");
//        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQo_bxse2bxFExLBG7om18lRUdbhRFKfJgRmpVb1XZHEQLmitpBZA");
//        imgURLs.add("https://o.aolcdn.com/images/dims3/GLOB/crop/7216x4733+0+281/resize/1028x675!/format/jpg/quality/85/http%3A%2F%2Fo.aolcdn.com%2Fhss%2Fstorage%2Fmidas%2Ff399350347cf76a6e1045da718c853ff%2F205833374%2Fholiday-turkey-dinner-picture-id836012728");
//        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbNDWFfTs0UYzONQyr6ij-wu0U0S-fszRj2yYvWWW3ZCxBDdcR");
//        imgURLs.add("https://www.divenq.co.uk/wp-content/uploads/2017/12/A5-Flyer-1080-1024x1024.jpg");
//        imgURLs.add("http://www.thecanadaguide.com/wp-content/uploads/top-photo-food.jpg");
//        imgURLs.add("https://lowcarbyum.com/wp-content/uploads/2017/11/keto-foods-list-bacon.jpg");
//        imgURLs.add("http://pluspng.com/img-png/food-png-food-salad-image-2962-428.png");
//        imgURLs.add("http://pluspng.com/img-png/png-plate-of-food--1502.png");
//        imgURLs.add("http://pluspng.com/img-png/food-png-hd-download-png-image-food-png-hd-1134.png");
//
//        setupImageGrid(imgURLs);
//    }

    //not working
//    public void setupImageGrid(ArrayList<String> imgURLs) {
//        GridView gridView = (GridView) findViewById(R.id.HomeGridView);

//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
//        gridView.setColumnWidth(imageWidth);

//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs); //issue with GridImage Adapter. Could be layout issue.
//        gridView.setAdapter(adapter);
//    }


    /**
     * Responsible for adding the 3 tabs: Camera, Home, Messages
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new CameraFragment());  //index 0
        adapter.addFragment(new HomeFragment()); //index 1
        adapter.addFragment(new MessagesFragment()); //index 2
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(0).setText("Menu");
        tabLayout.getTabAt(1).setText("Recipe");
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setCheckable(true);
    }


    /*
    ------------------------------------------ firebase ----------------------------------------
     */

    /**
     * checks to see if the @param 'user' is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null){
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        mViewPager.setCurrentItem(HOME_FRAGMENT);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }

}

