package com.nullandvoid.empowerment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nullandvoid.empowerment.databinding.FragmentBeempoweredBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmpowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmpowerFragment extends Fragment {
    OkHttpClient client = new OkHttpClient();
    private FragmentBeempoweredBinding binding;

    public static final String SHARED_PREFS = "shared_prefs";
    public Spinner itemSpinner;
    int parsedQuantity;
    public static final String USER_KEY = "user_key";
    public int quantity;

    public String selecteditem;
    public String bio;
    SharedPreferences sharedPreferences;
    public String userid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmpowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmpowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmpowerFragment newInstance(String param1, String param2) {
        EmpowerFragment fragment = new EmpowerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empower, container, false);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(USER_KEY, null);
        fetchItemsFromServer();
        itemSpinner = view.findViewById(R.id.itemSpinner);
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteditem = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Selected: " + selecteditem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        View donateBtn = view.findViewById(R.id.addbtn);
        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Bquantity = view.findViewById(R.id.quantity);
                String quantityText = Bquantity.getText().toString().trim();
                if (quantityText.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
                }
                try {
                    parsedQuantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
                }
                if (selecteditem == null || selecteditem.isEmpty()) {
                    Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                }
                add_balance(parsedQuantity, selecteditem, userid);
            }
        });
        Menu.hideProgressBar();
        return view;

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
                conn.disconnect();

                JSONArray jsonArray = new JSONArray(jsonResult.toString());
                ArrayList<String> items = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    items.add(jsonArray.getString(i));
                }

                // Update the Spinner on the UI thread
                if (getActivity() != null) { // Check if activity is still available
                    getActivity().runOnUiThread(() -> {
                        if (getContext() != null && itemSpinner != null) { // Additional safety checks
                            // *** THIS IS WHERE YOU APPLY THE CUSTOM LAYOUT ***
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    requireContext(),
                                    R.layout.custom_spinner_selected_item, // Your custom layout for the selected item
                                    items
                            );
                            // Set the layout for the Dropdown items (e.g., a standard Android layout)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            itemSpinner.setAdapter(adapter);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error fetching items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }

    public void add_balance(int quantity, String selecteditem, String userid) {

        RequestBody formBody = new FormBody.Builder()
                .add("Quantity", String.valueOf(quantity))
                .add("ItemId", selecteditem)
                .add("UserID", String.valueOf(userid))
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/CDonation.php")
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
                            Toast.makeText(getContext(), "Balance added", Toast.LENGTH_SHORT).show();
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