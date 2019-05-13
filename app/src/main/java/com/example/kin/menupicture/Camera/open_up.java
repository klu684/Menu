package com.example.kin.menupicture.Camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.kin.menupicture.ImageSearch.ImageDisplayActivity;

import com.example.kin.menupicture.ImageSearch.SearchActivity;
import com.example.kin.menupicture.R;

import java.util.ArrayList;

public class open_up extends AppCompatActivity {
    private ListView listOfTexts;
    ImageView image;

    private static String[] newList;
    //private static String[] translateList;
    Context myContext;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newfile);

        Intent newpass = getIntent();
        Bundle myBundle = newpass.getExtras();

        String paragraph= myBundle.getString("newPara");

        newList = paragraph.split("\n");

        listOfTexts = findViewById(R.id.input_v);
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.newfile,OcrGraphic.newList);
//        q.setAdapter(adapter);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,newList);
        listOfTexts.setAdapter(adapter);

        image = (ImageView)findViewById(R.id.imageView);
        byte[] imgByteArr = myBundle.getByteArray("imgByteArr");

        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length, new BitmapFactory.Options());

        image.setImageBitmap(bitmap);
        myContext = this;

        listOfTexts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent toSearch = new Intent(myContext, SearchActivity.class);

                Bundle myBundle = new Bundle();
                myBundle.putString("onTap",selectedItem);

                toSearch.putExtras(myBundle);

                startActivity(toSearch);
            }
        });
    }
}
