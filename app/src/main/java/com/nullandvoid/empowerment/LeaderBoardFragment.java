package com.nullandvoid.empowerment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nullandvoid.empowerment.ui.home.RequestUser;
import com.nullandvoid.empowerment.ui.home.RequestUserAdapter;
import com.nullandvoid.empowerment.ui.home.Requesttop_donatorsAdapter;
import com.nullandvoid.empowerment.ui.home.top_donators;

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

public class LeaderBoardFragment extends Fragment {
    private OkHttpClient client = new OkHttpClient();
    private RecyclerView recyclerView;
    private Requesttop_donatorsAdapter adapter;
    private LeaderBoardViewModel mViewModel;

    public static LeaderBoardFragment newInstance() {
        return new LeaderBoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        // Initialize RecyclerView after inflating the view
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new Requesttop_donatorsAdapter(new ArrayList<>(), requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Menu.hideProgressBar();
        getTopDonators();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LeaderBoardViewModel.class);
    }

    public void getTopDonators() {
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/leaderboard.php")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                    Menu.hideProgressBar();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        Menu.hideProgressBar();
                    });
                    return;
                }

                final String responseData = response.body().string();
                try {
                    JSONArray array = new JSONArray(responseData);
                    List<top_donators> tempList = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String name = obj.getString("Name");
                        String surname = obj.getString("Surname");
                        int num_donations = obj.getInt("Num_Donations"); // Better to use getInt for numbers
                        tempList.add(new top_donators(name, surname, num_donations));
                    }

                    requireActivity().runOnUiThread(() -> {
                        adapter.updateData(tempList);
                        Menu.hideProgressBar();
                    });
                } catch (JSONException e) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        Menu.hideProgressBar();
                    });
                }
            }
        });
    }
}