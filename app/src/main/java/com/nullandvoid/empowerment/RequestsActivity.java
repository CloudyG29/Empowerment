package com.nullandvoid.empowerment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class RequestsActivity extends AppCompatActivity {
    private ProgressBar loadingPB;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_KEY = "user_key";
    SharedPreferences sharedPreferences;
    String userid;
    public static JSONArray requests;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(USER_KEY, null);
        loadingPB = findViewById(R.id.progressBar);
        loadingPB.setVisibility(View.VISIBLE);
        fetchRequests(userid);

    }

    public void fetchRequests(String userid) {

        RequestBody formBody = new FormBody.Builder()
                .add("UserID", userid)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/NumRequests.php")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Toast.makeText(RequestsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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
                            if (responseData.trim().startsWith("[")) {
                                requests = new JSONArray(responseData);
                                RecyclerView recyclerView = findViewById(R.id.recyclerRequests);
                                List<RequestItem> requestList = new ArrayList<>();
                                for (int i = 0; i < requests.length(); i++) {
                                    try {
                                        JSONObject personMessage = requests.getJSONObject(i);
                                        String item = personMessage.getString("ItemID");
                                        String quantity = String.valueOf(personMessage.getInt("Quantity"));
                                        String bio = personMessage.getString("Biography");
                                        requestList.add(new RequestItem(item, quantity, bio));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));
                                RequestAdapter adapter = new RequestAdapter(requestList);
                                recyclerView.setAdapter(adapter);

                            } else if (responseData.trim().startsWith("{")) {
                                JSONObject obj = new JSONObject(responseData);
                                if (obj.has("status")) {
                                    System.out.println("Message: " + obj.getString("status"));
                                }
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