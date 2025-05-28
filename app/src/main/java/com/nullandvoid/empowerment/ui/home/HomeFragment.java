package com.nullandvoid.empowerment.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nullandvoid.empowerment.LoginActivity;
import com.nullandvoid.empowerment.Menu;
import com.nullandvoid.empowerment.databinding.FragmentHomeBinding;
import com.nullandvoid.empowerment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    public HomeFragment(){

    }

    private FragmentHomeBinding binding;
    public Spinner myyspinner;
    OkHttpClient client=new OkHttpClient();
    private RecyclerView recyclerView;
    private RequestUserAdapter adapter;
    public String selecteditem;


    private List<RequestUser> userList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myyspinner = root.findViewById(R.id.myyspinner);
        recyclerView = root.findViewById(R.id.requestRecyclerView);
        adapter = new RequestUserAdapter(userList, requireContext(), selecteditem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

        loadSpinnerItems();
        myyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) myyspinner.getSelectedItem();
                selecteditem=selectedItem;
                getusers(selecteditem,adapter);
                adapter.setSelectedItem(selecteditem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fetchItemsFromServer();

        return root;
    }

    private void loadSpinnerItems() {
        new Thread(() -> {
            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2801257/itemget.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
                reader.close();

                JSONArray jsonArray = new JSONArray(result.toString());
                List<String> items = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    items.add(jsonArray.getString(i));
                }

                requireActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, items);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    myyspinner.setAdapter(spinnerAdapter);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchItemsFromServer() {
        new Thread(() -> {
            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2801257/itemget.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResult = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResult.append(line);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(jsonResult.toString());
                ArrayList<String> items = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    items.add(jsonArray.getString(i));
                }

                requireActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    myyspinner.setAdapter(adapter);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void getusers(String selecteditem,RequestUserAdapter adapter) {

        RequestBody formBody = new FormBody.Builder()
                .add("ItemName", selecteditem)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/getRequest.php")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                try {
                    JSONArray array=new JSONArray(responseData);
                    List<RequestUser> tempList=new ArrayList<>();

                    if (array.length() == 0) {
                        requireActivity().runOnUiThread(() -> {
                            adapter.updateData(new ArrayList<>()); // ✅ Clear the RecyclerView
                            Toast.makeText(getContext(), "No users requested this item.", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }


                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        String name = obj.getString("Name");
                        String surname = obj.getString("Surname");
                        int quantity = Integer.parseInt(obj.getString("Quantity"));
                        String biography = obj.getString("Biography");

                        tempList.add(new RequestUser(name, surname, quantity, biography));
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        adapter.updateData(tempList);  // This updates the RecyclerView
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show());
                }

                }
            });
        }
}
