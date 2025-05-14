package com.nullandvoid.empowerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
     public static JSONObject person_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button b = findViewById(R.id.loginbtn);
        b.setOnClickListener(v -> {
            login(v);
        });
    }

    public void Login(String email, String password) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/Login.php")
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
                        try{

                            JSONObject person= new JSONObject(responseData);
                            person_2 = person;
                            int size = person.length();
                            if(size == 1 ) {
                                if(person.getString("error").equals("Incorrect password.")){
                                    Toast.makeText(LoginActivity.this, "Wrong  password", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else {

                                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(LoginActivity.this, Menu.class);
                                startActivity(intent);
                                finish();

                            }




                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        });
    }
    public void goSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(@NonNull View view) {
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        String Password = "";
        String UserEmail = "";
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            Password = password.getText().toString();
            UserEmail = email.getText().toString();
        }
        else {
            Toast.makeText(LoginActivity.this, "Empty field!", Toast.LENGTH_SHORT).show();

        }
        Login(UserEmail, Password);
    }

}