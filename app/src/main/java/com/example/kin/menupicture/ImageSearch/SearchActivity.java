package com.example.kin.menupicture.ImageSearch;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kin.menupicture.R;
import com.example.kin.menupicture.Utils.ImageResultArrayAdapter;
import com.example.kin.menupicture.helpers.BottomNavigationViewHelper;
import com.example.kin.menupicture.helpers.EndlessScrollListener;
import com.example.kin.menupicture.models.ImageFilter;
import com.example.kin.menupicture.models.ImageResult;
import com.example.kin.menupicture.Net.SearchClient;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private static int MAX_PAGE = 3;
    EditText etQuery;
    GridView gvResults;
    ArrayList<ImageResult> imageResults;
    ImageResultArrayAdapter imageAdapter;
    SearchClient client;
    int startPage = 1;
    String query;
    ImageFilter imageFilter = new ImageFilter();

    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = SearchActivity.this;

    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);

        Intent toSearch = getIntent();
        Bundle myBundle = toSearch.getExtras();

        String selected= myBundle.getString("onTap");
        etQuery.setText(selected, TextView.BufferType.EDITABLE);

        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyboard(v);
                    onImageSearch(1);
                    return true;
                }
                return false;
            }
        });

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult imageResult = imageResults.get(position);
                i.putExtra("result", imageResult);
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page <= MAX_PAGE) {
                    onImageSearch((10*(page-1)) + 1);
                }
            }
        });

        imageResults = new ArrayList<>();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);

        setupBottomNavigationView();

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

    public void onImageSearch(int start) {

        if (isNetworkAvailable()) {
            client = new SearchClient();
            query = etQuery.getText().toString();
            startPage = start;
            if (startPage == 1)
                imageAdapter.clear();

            if (!query.equals(""))
                client.getSearch(query, startPage, this, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    JSONArray imageJsonResults;
                                    if (response != null) {
                                        imageJsonResults = response.getJSONArray("items");
                                        imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), R.string.invalid_data, Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Toast.makeText(getApplicationContext(), R.string.service_unavailable, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            else {
                Toast.makeText(this, R.string.invalid_query, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void onFinishDialog(ImageFilter objFilter){
        imageFilter = objFilter;
        onImageSearch(1);
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
