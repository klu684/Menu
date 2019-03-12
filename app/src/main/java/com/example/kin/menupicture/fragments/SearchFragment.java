package com.example.kin.menupicture.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kin.menupicture.Dialogs.ImageFilterDialog;
import com.example.kin.menupicture.R;
import com.example.kin.menupicture.activities.ImageDisplayActivity;
import com.example.kin.menupicture.adapters.ImageResultArrayAdapter;
import com.example.kin.menupicture.helpers.EndlessScrollListener;
import com.example.kin.menupicture.models.ImageFilter;
import com.example.kin.menupicture.models.ImageResult;
import com.example.kin.menupicture.net.SearchClient;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class SearchFragment extends Fragment{

    private static int MAX_PAGE = 2;
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<ImageResult> imageResults;
    ImageResultArrayAdapter imageAdapter;
    SearchClient client;
    int startPage = 1;
    String query;
    ImageFilter imageFilter = new ImageFilter();

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v= inflater.inflate(R.layout.fragment_search, container, false);

        etQuery = (EditText) v.findViewById(R.id.etQuery);
        gvResults = (GridView) v.findViewById(R.id.gvResults);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
                onImageSearch(1);
            }
        });

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), ImageDisplayActivity.class);
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
        imageAdapter = new ImageResultArrayAdapter(this.getContext(), imageResults);
        gvResults.setAdapter(imageAdapter);

        return v;
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v= inflater.inflate(R.layout.fragment_search, container, false);
//
////        //These line of code goes from one fragment to another fragment
////        Fragment selectedFragment = new AboutFragment();
////        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                selectedFragment).commit();
//
//
//        return v;
//    }

    public void onImageSearch(int start) {

        if (isNetworkAvailable()) {
            client = new SearchClient();
            query = etQuery.getText().toString();
            startPage = start;
            if (startPage == 1)
                imageAdapter.clear();

            if (!query.equals(""))
                client.getSearch(query, startPage, this.getContext(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    JSONArray imageJsonResults;
                                    if (response != null) {
                                        imageJsonResults = response.getJSONArray("items");
                                        imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_data, Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Toast.makeText(getActivity().getApplicationContext(), R.string.service_unavailable, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            else {
                Toast.makeText(this.getActivity(), R.string.invalid_query, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this.getActivity(),R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void onFinishDialog(ImageFilter objFilter){
        imageFilter = objFilter;
        onImageSearch(1);
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
