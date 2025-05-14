package com.nullandvoid.empowerment.ui;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nullandvoid.empowerment.LoginActivity;
import com.nullandvoid.empowerment.Menu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Returns {
    OkHttpClient client = new OkHttpClient();
    public int user_ID;


    // Constructor
    public Returns() throws JSONException {
        JSONObject person_3 = LoginActivity.person_2;

        if (person_3 != null) {
            user_ID = person_3.getInt("user_id");

        } else {
            throw new JSONException("Login was unsuccesful");
        }
    }



     public void Donate() {

         RequestBody formBody = new FormBody.Builder()
                 .add("userid", String.valueOf(user_ID))
                 .add("quantity", String.valueOf(quantity))
                 .add("itemid", item_name)
                 .build();

         Request request = new Request.Builder()
                 .url("https://lamp.ms.wits.ac.za/home/s2801257/CDonation.php")
                 .post(formBody)
                 .build();
         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(@NonNull Call call, @NonNull IOException e) {
                 e.printStackTrace();
             }

             @Override
             public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                 if (!response.isSuccessful()) {
                     throw new IOException("Unexpected code " + response);
                 }

                 final String responseData = response.body().string();
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {


                         JSONObject person = null;
                         try {
                             person = new JSONObject(responseData);
                         } catch (JSONException e) {
                             throw new RuntimeException(e);
                         }
                         try {
                             if (person.getString("status").equals("error")) {
                                 Toast.makeText(Donate.this, "donation failed", Toast.LENGTH_SHORT).show();
                             } else {
                                 Toast.makeText(LoginActivity.this, "Donation added", Toast.LENGTH_SHORT).show();
                             }
                         } catch (JSONException e) {
                             throw new RuntimeException(e);
                         }
                     }


                 }