package com.nullandvoid.empowerment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
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

public class MainActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button b = findViewById(R.id.loginbtn);
        b.setOnClickListener(v -> {
            login(v);
        });
        Button b2 = findViewById(R.id.b3);
        b2.setOnClickListener(v -> {
            register(v);
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
                            int size = person.length();
                            if(size == 1 ) {
                                if(person.getString("error").equals("Incorrect password.")){
                                Toast.makeText(MainActivity.this, "Wrong  password", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else {
                                TextView v1 = findViewById(R.id.View10);
                                TextView v2 = findViewById(R.id.View11);
                                TextView v3 = findViewById(R.id.View12);
                                TextView v4 = findViewById(R.id.View13);

                                v1.setText(person.getString("Name"));
                                v2.setText(person.getString("Surname"));
                                v3.setText(person.getString("Email"));
                                v4.setText(person.getString("UserID"));
                            }




                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        });
    }

    private void Register(String name, String surname, String email, String password) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("name", name)
                .add("surname", surname)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/Register.php")
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
                        try {
                            JSONObject person = new JSONObject(responseData);
                            Toast.makeText(MainActivity.this, person.getString("status"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
            }
        });
    }
    public void login(@NonNull View view) {
        EditText email = findViewById(R.id.user);
        EditText password = findViewById(R.id.password);
        String Password = "";
        String UserEmail = "";
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            Password = password.getText().toString();
            UserEmail = email.getText().toString();
        }
        else {
            //Toast.makeText(getContext(), "Empty field!", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Empty field!", Toast.LENGTH_SHORT).show();

        }
        Login(UserEmail, Password);
    }
    public void register(@NonNull View view) {
        EditText name = findViewById(R.id.send_name);
        EditText surname = findViewById(R.id.send_surname);
        EditText email = findViewById(R.id.user);
        EditText password = findViewById(R.id.password);
        String Password = "";
        String UserEmail = "";
        String Name = "";
        String Surname = "";
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            Password = password.getText().toString();
            UserEmail = email.getText().toString();
            Name = name.getText().toString();
            Surname = surname.getText().toString();
        }
        else {
            //Toast.makeText(getContext(), "Empty field!", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Empty field!", Toast.LENGTH_SHORT).show();

    }
        Register(Name, Surname, UserEmail, Password);
    }
}



