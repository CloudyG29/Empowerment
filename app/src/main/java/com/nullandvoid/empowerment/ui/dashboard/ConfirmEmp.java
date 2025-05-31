package com.nullandvoid.empowerment.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.nullandvoid.empowerment.Menu;
import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.ui.home.RequestUserAdapter;

import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
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
    public static int offeredQ;
    String donater_userid, donater_email,  donater_name, donater_surname;
    String donatee_userid, donatee_email, donatee_name, donatee_surname,donatee_quantity,donatee_biography;
    String itemName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_emp);
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> {
            Menu.hideProgressBar();
            finish();

        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            String name=getIntent().getStringExtra("name");
            String surname=getIntent().getStringExtra("surname");
            //String quantity=getIntent().getStringExtra("quantity");
            String quantity= String.valueOf(RequestUserAdapter.q);
            String biography=getIntent().getStringExtra("biography");
             itemName=getIntent().getStringExtra("selectedItem");
            TextView name_of=findViewById(R.id.name_of);
            name_of.setText(name+" "+surname);
            donatee_name = name;
            donatee_surname = surname;
            TextView quantity_of=findViewById(R.id.quantity_of);
            quantity_of.setText(itemName+": "+ quantity);
            TextView bio_of=findViewById(R.id.bio_of);
            bio_of.setText("Biography:\n\n"+ biography);

            String photoUrl = getIntent().getStringExtra("photoUrl");
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .into((CircleImageView) findViewById(R.id.profile_image2));
            Button donate = findViewById(R.id.donate);

            sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            donater_userid = sharedPreferences.getString(USER_KEY, null);
            donater_email = sharedPreferences.getString(EMAIL_KEY, null);
            get_userid();
            donate.setOnClickListener(v1 -> {
                EditText lquantity = findViewById(R.id.quantity_willing);
                String input = lquantity.getText().toString().trim();

                if (input.isEmpty()) {
                    Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmEmp.this);

                    builder.setMessage("Are you sure want to donate " + itemName + " of quantity: " + lquantity.getText() + " to " + name + "?");

                    builder.setTitle("Donating?");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        offeredQ = Integer.parseInt(input);
                        final_donation();

                    });

                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();



                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid quantity entered", Toast.LENGTH_SHORT).show();
                }

                Menu.hideProgressBar();
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
                .add("Quantity", String.valueOf(offeredQ))
                .add("ItemName", itemName)
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
                            add_Donation();
                            Menu.hideProgressBar();
                            finish();
                        } else {
                            String s = "";
                            s = json.getString("status");
                            s = s.replace("ITEM", itemName);
                            Toast.makeText(ConfirmEmp.this, s, Toast.LENGTH_SHORT).show();
                            System.out.println(responseData);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ConfirmEmp.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    public  void add_Donation() {
            RequestBody formBody = new FormBody.Builder()
                    .add("DonorID", String.valueOf(donater_userid))
                    .add("DonateeID", String.valueOf(donatee_userid))
                    .add("Quantity", String.valueOf(offeredQ))
                    .add("Email", donater_email)
                    .add("ItemName", itemName)
                    .build();

            Request request = new Request.Builder()
                    .url("https://lamp.ms.wits.ac.za/home/s2801257/MergeUsers.php")
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
                                //Toast.makeText(ConfirmEmp.this, "Unsuccessful Donation", Toast.LENGTH_SHORT).show();
                                System.out.println("FAILED TO ADD DONATION TO NOTIFICATIONS");
                            } else if(json.getString("status").equals("Success"))  {
                                //Toast.makeText(ConfirmEmp.this, "Successful Donation", Toast.LENGTH_SHORT).show();
                                System.out.println("ADDED DONATION TO NOTIFICATIONS");
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

