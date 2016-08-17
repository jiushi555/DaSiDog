package com.example.DasiDog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.add.MultipartEntity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class JSONParser {

static InputStream is = null;
static JSONObject jObj = null;
static String json = "";

// constructor
public JSONParser() {

}

// function get json from url
// by making HTTP POST or GET method
public JSONObject upLoad(String url, String method,
        List<NameValuePair> params,Context context,InputStream in,String imgname){
	// Making HTTP request
    try {

        // check for request method
        if(method == "POST"){
            // request method is POST
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(params, "UTF-8");
            
            url += "?" + paramString;
            MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("myFile", imgname, in);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setEntity(reqEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        }else if(method == "GET"){
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(params, "UTF-8");
//            String paramString_=URLDecoder.decode(paramString, "UTF-8");
            url += "?" + paramString;
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        }           

    } catch (UnsupportedEncodingException e) {
    	Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    } catch (ClientProtocolException e) {
    	Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    } catch (IOException e) {
    	Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "UTF-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
       
        json = sb.toString();
        Log.d("a",json);
    } catch (Exception e) {
        Log.e("Buffer Error", "Error converting result " + e.toString());
    }

    // try parse the string to a JSON object
    try {
        jObj = new JSONObject(json);
    } catch (JSONException e) {

        Log.e("JSON Parser", "Error parsing data " + e.toString());
    }

    // return JSON String
    return jObj;
}
public JSONObject makeHttpRequest(String url, String method,
        List<NameValuePair> params,Context context) {

    // Making HTTP request
    try {

        // check for request method
        if(method == "POST"){
            // request method is POST
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(params, "UTF-8");
            
            url += "?" + paramString;
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        }else if(method == "GET"){
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(params, "UTF-8");
//            String paramString_=URLDecoder.decode(paramString, "UTF-8");
            url += "?" + paramString;
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        }           

    } catch (UnsupportedEncodingException e) {
    	Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    } catch (ClientProtocolException e) {
    	Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    } catch (IOException e) {
    	Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "UTF-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
       
        json = sb.toString();
        Log.d("a",json);
    } catch (Exception e) {
        Log.e("Buffer Error", "Error converting result " + e.toString());
    }

    // try parse the string to a JSON object
    try {
        jObj = new JSONObject(json);
    } catch (JSONException e) {

        Log.e("JSON Parser", "Error parsing data " + e.toString());
    }

    // return JSON String
    return jObj;
}
}