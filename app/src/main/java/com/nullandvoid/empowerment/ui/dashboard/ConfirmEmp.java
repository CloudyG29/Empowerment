package com.nullandvoid.empowerment.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.nullandvoid.empowerment.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmEmp extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    OkHttpClient client = new OkHttpClient();
    public static final String EMAIL_KEY = "email_key";
    public static final String NAME_KEY = "name_key";
    public static final String USER_KEY = "user_key";
    public static final String SURNAME_KEY = "surname_key";
    int quantity;
    String donater_userid, donater_email,  donater_name, donater_surname;
    String donatee_userid, donatee_email, donatee_name, donatee_surname,donatee_quantity,donatee_biography;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_emp);
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> {
            finish(); // Close ConfirmEmp and return to HomeFragment
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
             donatee_name=getIntent().getStringExtra("name");
           donatee_surname=getIntent().getStringExtra("surname");
             donatee_biography=getIntent().getStringExtra("biography");
            get_userid();



            TextView name_of=findViewById(R.id.name_of);name_of.setText(donatee_name);
            TextView surname_of=findViewById(R.id.surname_of);surname_of.setText(donatee_surname);
            TextView quantity_of=findViewById(R.id.quantity_of);quantity_of.setText(donatee_quantity);
            TextView bio_of=findViewById(R.id.bio_of);bio_of.setText(donatee_biography);

            sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            donater_userid = sharedPreferences.getString(USER_KEY, null);
            donater_email = sharedPreferences.getString(EMAIL_KEY, null);
            donater_name = sharedPreferences.getString(NAME_KEY, null);
            donater_surname = sharedPreferences.getString(SURNAME_KEY, null);
            Button donate = findViewById(R.id.donate);
            donate.setOnClickListener(v1 -> {
                EditText lquantity = findViewById(R.id.quantity_willing);
                String input = lquantity.getText().toString().trim();

                if (input.isEmpty()) {
                    Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                quantity = Integer.parseInt(input);
                final_donation();
            });


            return insets;
        });
    }
    public void get_userid() {
        RequestBody formBody = new FormBody.Builder()
                .add("name", donatee_name)
                .add("surname", donatee_surname)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/getuserid.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(ConfirmEmp.this, "Network error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() ->
                            Toast.makeText(ConfirmEmp.this, "server error " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                final String responseData = response.body().string();

               runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(responseData);
                         if (json.has("error")) {
                            Toast.makeText(ConfirmEmp.this, "Could not find user", Toast.LENGTH_SHORT).show();
                        } else {
                            donatee_userid = json.getString("userId");
                            donatee_email = json.getString("email");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ConfirmEmp.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void final_donation() {
        RequestBody formBody = new FormBody.Builder()
                .add("DonorID", String.valueOf(donater_userid))
                .add("DonateeID", String.valueOf(donatee_userid))
                .add("Quantity", String.valueOf(quantity))
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/FinalDonation.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(ConfirmEmp.this, "Network error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() ->
                            Toast.makeText(ConfirmEmp.this, "server error " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                final String responseData = response.body().string();

                runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(responseData);
                        if (json.getString("status").equals("Error")) {
                            Toast.makeText(ConfirmEmp.this, "Donation Unsuccessful", Toast.LENGTH_SHORT).show();
                        } else if(json.getString("status").equals("Success"))  {
                           Toast.makeText(ConfirmEmp.this, "Donation Successful", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ConfirmEmp.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}