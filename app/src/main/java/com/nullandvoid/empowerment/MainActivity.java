package com.nullandvoid.empowerment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        Button b = view.findViewById(R.id.loginbtn);
        b.setOnClickListener(v -> {
            login(v);
        });
    }

    private void Login(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2801257/Login.php")
                .newBuilder()
                .addQueryParameter("email", email)
                .addQueryParameter("password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        String s;
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

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> myTextView.setText(responseData));
                    //Do something here
                }
            }
        });
    }


    public void login(@NonNull View view) {
        String Password = "";
        String UserEmail = "";
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            Password = password.getText().toString();
            UserEmail = email.getText().toString();
        }
        else {
            Toast.makeText(getContext(), "Empty field!", Toast.LENGTH_SHORT).show();
        }
        Login(UserEmail, Password);
    }
}