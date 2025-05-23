package com.nullandvoid.empowerment.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nullandvoid.empowerment.databinding.FragmentHomeBinding;
import com.nullandvoid.empowerment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public Spinner myyspinner;
    private RecyclerView requestRecyclerView;
    private RequestUserAdapter requestAdapter;
    private RequestUserAdapter adapter;

    private List<RequestUser> userList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myyspinner = root.findViewById(R.id.myyspinner);
        requestRecyclerView = root.findViewById(R.id.requestRecyclerView);
        adapter = new RequestUserAdapter(userList);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRecyclerView.setAdapter(adapter);

        loadSpinnerItems();
        myyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) myyspinner.getSelectedItem();
                fetchUsersForItem(selectedItem);
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

    private void fetchUsersForItem(String itemid) {
        new Thread(() -> {
            try {
                URL url = new URL("http://your_ip_or_domain/your_path/get_requests.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = "itemid=" + URLEncoder.encode(itemid, "UTF-8");
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                conn.disconnect();

                JSONArray jsonArray = new JSONArray(sb.toString());

                userList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String name = obj.getString("Name");
                    String surname = obj.getString("Surname");
                    int quantity = obj.getInt("Quantity");
                    String bio = obj.getString("Biography");

                    userList.add(new RequestUser(name, surname, quantity, bio));
                }

                requireActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Users loaded: " + userList.size(), Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
}
