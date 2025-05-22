package com.nullandvoid.empowerment.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.databinding.FragmentBeempoweredBinding;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BeEmpoweredFragment extends Fragment {

    private FragmentBeempoweredBinding binding;
    public Spinner itemSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BeEmpoweredViewModel beEmpoweredViewModel =
                new ViewModelProvider(this).get(BeEmpoweredViewModel.class);

        binding = FragmentBeempoweredBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textDashboard;
        beEmpoweredViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        fetchItemsFromServer();
        itemSpinner = root.findViewById(R.id.itemSpinner);
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
}