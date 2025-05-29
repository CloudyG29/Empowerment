package com.nullandvoid.empowerment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String NAME_KEY = "name_key";
    public static final String USER_KEY = "user_key";
    public static final String SURNAME_KEY = "surname_key";
    private ProgressBar loadingPB;

    SharedPreferences sharedPreferences;
    String userid, email, name, surname;
    ProgressBar loader;
    OkHttpClient client = new OkHttpClient();
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
        loader = findViewById(R.id.progressBar2);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(USER_KEY, null);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        name = sharedPreferences.getString(NAME_KEY, null);
        surname = sharedPreferences.getString(SURNAME_KEY, null);

        if(userid != null && email != null && name != null && surname != null) {
            Intent i = new Intent(LoginActivity.this, Menu.class);
            startActivity(i);
            finish();
        }

        Button b = findViewById(R.id.loginbtn);
        b.setOnClickListener(v -> {
            loader.setVisibility(VISIBLE);
            login(v);
        });

    }

    public void Login(String Email, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", Email)
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
                runOnUiThread(() -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        loader.setVisibility(GONE);
                        Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }, 6000);
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    //TODO: Someone must handle this so that the app doesnt crash when there is no internet connection
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
                                if (person.has("error")) {
                                    String error = person.getString("error");
                                    if (error.equals("Please verify your email address before logging in.")) {
                                        Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                    } else if (error.equals("Incorrect password.")) {
                                        Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            else {
                                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USER_KEY, person.getString("UserID"));
                                editor.putString(EMAIL_KEY, person.getString("Email"));
                                editor.putString(NAME_KEY, person.getString("Name"));
                                editor.putString(SURNAME_KEY, person.getString("Surname"));
                                editor.apply();

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
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        loader.setVisibility(VISIBLE);
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