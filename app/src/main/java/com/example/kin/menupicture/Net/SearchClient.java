package com.example.kin.menupicture.Net;

import android.content.Context;
import android.widget.Toast;

import com.example.kin.menupicture.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchClient {
    private static final String API_BASE_URL = "https://www.googleapis.com/customsearch/v1?";
    private static final String API_KEY = "AIzaSyAcM7NQsbXX331W4s0VJYntkYLTGMxF5Vw";
    private static final String CX_KEY = "018166152475396321462:neztg-w1huk";
    private AsyncHttpClient client;

    public SearchClient(){
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl){
        return API_BASE_URL + relativeUrl;   }

    public void getSearch(final String query, int startPage, Context context, JsonHttpResponseHandler handler ){
        try {
            String url = getApiUrl("q="+ URLEncoder.encode(query,"utf-8")+"&start="+startPage+
                    "&cx="+CX_KEY+"&searchType=image"+"&key="+API_KEY);
            client.get(url, handler);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Toast.makeText(context, R.string.search_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}
