package com.nullandvoid.empowerment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_KEY = "user_key";
    SharedPreferences sharedPreferences;
    String userid;
    public static JSONArray messages;
    private ProgressBar loadingPB;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btn = findViewById(R.id.button4);
        btn.setOnClickListener(v -> {
            finish();
        });
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(USER_KEY, null);
        loadingPB = findViewById(R.id.progressBar);
        loadingPB.setVisibility(View.VISIBLE);
        fetchMessages(userid);


    }


    public void fetchMessages(String userid) {
        System.out.println(userid);
        RequestBody formBody = new FormBody.Builder()
                .add("UserID", userid)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/Notifications.php")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Toast.makeText(NotificationsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                System.out.println(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject obj = new JSONObject(responseData);
                            if (obj.getString("Status").equals("Success")) {
                                messages = obj.getJSONArray("Donations");
                                RecyclerView recyclerView = findViewById(R.id.recyclerMessages);
                                List<MessageItem> messageList = new ArrayList<>();
                                for (int i = 0; i < messages.length(); i++) {
                                    try {
                                        JSONObject personMessage = messages.getJSONObject(i);
                                        String name = personMessage.getString("Name");
                                        String surname = personMessage.getString("Surname");
                                        String item = personMessage.getString("ItemID");
                                        String quantity = String.valueOf(personMessage.getInt("Quantity"));
                                        String email = personMessage.getString("Email");
                                        messageList.add(new MessageItem(name, item, quantity, email, surname));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
                                MyAdapter adapter = new MyAdapter(messageList);
                                recyclerView.setAdapter(adapter);

                            } else if (obj.has("Status")) {
                                    TextView t = new TextView(NotificationsActivity.this);
                                    t.setText(obj.getString("Status"));
                                    LinearLayout l = findViewById(R.id.NoMessages);
                                    l.addView(t);
                                    System.out.println("Message: " + obj.getString("Status"));
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        loadingPB.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
}