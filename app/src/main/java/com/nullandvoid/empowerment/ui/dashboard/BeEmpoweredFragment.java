package com.nullandvoid.empowerment.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nullandvoid.empowerment.LoginActivity;
import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.databinding.FragmentBeempoweredBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BeEmpoweredFragment extends Fragment {

    private FragmentBeempoweredBinding binding;
    OkHttpClient client = new OkHttpClient();
    public static final String SHARED_PREFS = "shared_prefs";
    public Spinner itemSpinner;
    public static final String USER_KEY = "user_key";
    public int quantity;

    public String selecteditem;
    public String bio;
    SharedPreferences sharedPreferences;
    public String userid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BeEmpoweredViewModel beEmpoweredViewModel =
                new ViewModelProvider(this).get(BeEmpoweredViewModel.class);

        binding = FragmentBeempoweredBinding.inflate(inflater, container, false);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(USER_KEY, null);
        View root = binding.getRoot();


        fetchItemsFromServer();
        itemSpinner = root.findViewById(R.id.itemSpinner);
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteditem = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Selected: " + selecteditem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional
            }
        });

        /*
        EditText Bquantity = root.findViewById(R.id.quantity);
        EditText Bbio = root.findViewById(R.id.bio);

        String bio = Bbio.getText().toString().trim();
        String quantityText = Bquantity.getText().toString().trim();

        if (bio.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a biography", Toast.LENGTH_SHORT).show();

        }


        if (quantityText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();

        }

        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number entered for quantity", Toast.LENGTH_SHORT).show();

        }
        */
        View donateBtn = root.findViewById(R.id.getbtn);
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Bquantity = root.findViewById(R.id.quantity);
                EditText Bbio = root.findViewById(R.id.bio);

                String bioInput = Bbio.getText().toString().trim();
                String quantityText = Bquantity.getText().toString().trim();

                if (bioInput.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a biography", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (quantityText.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                int parsedQuantity;
                try {
                    parsedQuantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selecteditem == null || selecteditem.isEmpty()) {
                    Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                    return;
                }

                donate_request(parsedQuantity, bioInput, selecteditem, userid);
            }
        });



        return root;
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

                // Update the Spinner on the UI thread
                requireActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void donate_request(int quantity, String bio, String selecteditem, String userid) {
        System.out.println(userid);
        System.out.println(quantity);
        System.out.println(bio);
        System.out.println(selecteditem);
        RequestBody formBody = new FormBody.Builder()
                .add("Biography", bio)
                .add("Quantity", String.valueOf(quantity))
                .add("ItemId", selecteditem)
                .add("UserID", String.valueOf(userid))
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/CRequest.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "server error " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                final String responseData = response.body().string();

                requireActivity().runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(responseData);

                        if (json.has("success")) {
                            Toast.makeText(getContext(), "Item inserted successfully!", Toast.LENGTH_SHORT).show();
                        } else if (json.has("error")) {
                            Toast.makeText(getContext(), "Error: " + json.getString("error"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Unexpected response", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

