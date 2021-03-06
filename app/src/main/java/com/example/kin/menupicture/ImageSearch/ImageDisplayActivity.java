package com.example.kin.menupicture.ImageSearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kin.menupicture.R;
import com.example.kin.menupicture.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_image_display);
        ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
       // TextView tvImageName = (TextView) findViewById(R.id.tvImageName);

        ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        String url = imageResult.getFullUrl();
        Picasso.with(this).load(url).into(ivImage);
        //tvImageName.setText(imageResult.getTitle());

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_image_display, menu);
//        return true;
//    }
}
