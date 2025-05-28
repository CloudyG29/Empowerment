package com.nullandvoid.empowerment;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.text.TextUtils; // Import for TextUtils
import android.util.Patterns;


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

public class SignupActivity extends AppCompatActivity {
    ProgressBar loader;
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loader = findViewById(R.id.progressBar2);
       Button b2 = findViewById(R.id.sign_button);
        b2.setOnClickListener(v -> {
            loader.setVisibility(VISIBLE);
            register(v);
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
                            Toast.makeText(SignupActivity.this, person.getString("status"), Toast.LENGTH_SHORT).show();
                            if(person.getString("status").equals("success")) {
                                Toast.makeText(SignupActivity.this, "Check your email to verify your account.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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

    private boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    public void register(@NonNull View view) {
        boolean allValid = false;
        EditText name = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.password);
        EditText passwordConfirm = findViewById(R.id.passwordConfirm);
        TextView passInfo = findViewById(R.id.passInfo);
        String Password = "";
        String UserEmail = "";
        String Name = "";
        String Surname = "";
        String PasswordConfirm = "";

        name.setBackgroundResource(R.drawable.edittext);
        surname.setBackgroundResource(R.drawable.edittext);
        passInfo.setTextColor(Color.DKGRAY); // Reset to default or your 'normal' color
        email.setBackgroundResource(R.drawable.edittext); // Reset background (use your default)
        password.setBackgroundResource(R.drawable.edittext);
        passwordConfirm.setBackgroundResource(R.drawable.edittext);

        if(!email.getText().toString().equals("") && !password.getText().toString().equals("") && !name.getText().toString().equals("") && !surname.getText().toString().equals("") && !passwordConfirm.getText().toString().equals("")) {

            if (!isValidEmail(email.getText().toString())) {
                email.setBackgroundResource(R.drawable.erroredit); // Highlight email field
                Toast.makeText(SignupActivity.this, "Invalid email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.getText().toString().length() < 8) {
                passInfo.setTextColor(Color.parseColor("#FF0000"));
                password.setBackgroundResource(R.drawable.erroredit);
                Toast.makeText(SignupActivity.this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.getText().toString().matches(".*[A-Z].*")) {
                passInfo.setTextColor(Color.parseColor("#FF0000"));
                password.setBackgroundResource(R.drawable.erroredit);
                Toast.makeText(SignupActivity.this, "Password must contain at least one uppercase letter!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.getText().toString().matches(".*[a-z].*")) {
                passInfo.setTextColor(Color.parseColor("#FF0000"));
                password.setBackgroundResource(R.drawable.erroredit);
                Toast.makeText(SignupActivity.this, "Password must contain at least one lowercase letter!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.getText().toString().matches(".*\\d.*")) {
                passInfo.setTextColor(Color.parseColor("#FF0000"));
                password.setBackgroundResource(R.drawable.erroredit);
                Toast.makeText(SignupActivity.this, "Password must contain at least one number!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.getText().toString().matches(".*[!@#$%^&*()].*")) {
                passInfo.setTextColor(Color.parseColor("#FF0000"));
                password.setBackgroundResource(R.drawable.erroredit);
                Toast.makeText(SignupActivity.this, "Password must contain at least one special character!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
                passInfo.setTextColor(Color.parseColor("#FF0000"));
                password.setBackgroundResource(R.drawable.erroredit);
                Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Password = password.getText().toString();
            }
            UserEmail = email.getText().toString();
            Name = name.getText().toString();
            Surname = surname.getText().toString();
        }
        else {
            password.setBackgroundResource(R.drawable.erroredit);
            passwordConfirm.setBackgroundResource(R.drawable.erroredit);
            Toast.makeText(SignupActivity.this, "Empty field!", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(name.getText().toString())) name.setBackgroundResource(R.drawable.erroredit);
            if (TextUtils.isEmpty(surname.getText().toString())) surname.setBackgroundResource(R.drawable.erroredit);
            if (TextUtils.isEmpty(email.getText().toString())) email.setBackgroundResource(R.drawable.erroredit);
            if (TextUtils.isEmpty(password.getText().toString())) password.setBackgroundResource(R.drawable.erroredit);
            if (TextUtils.isEmpty(passwordConfirm.getText().toString())) passwordConfirm.setBackgroundResource(R.drawable.erroredit);
            return;

    }
        allValid = true;
        if (allValid) {
            Register(Name, Surname, UserEmail, Password);
        }
    }

    public void goLogin(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        loader.setVisibility(VISIBLE);
        startActivity(intent);
        finish();
    }

}



