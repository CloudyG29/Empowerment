package com.nullandvoid.empowerment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    EditText item_name; ;
    EditText quantity;
    public int user_ID;
    OkHttpClient client = new OkHttpClient();


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_empower, container, false);
        item_name = view.findViewById(R.id.Item_name);
        quantity = view.findViewById(R.id.quantity);

        return view;
    }

    public void returns() throws JSONException {
        JSONObject person_3 = LoginActivity.person_2;
        if (person_3 != null) {
            user_ID = person_3.getInt("user_id");
        }
    }
    public void Donate() {

        RequestBody formBody = new FormBody.Builder()
                .add("userid", String.valueOf(user_ID))
                .add("quantity", String.valueOf(quantity))
                .add("itemid", String.valueOf(item_name))
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/CDonation.php")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject person;
                        try {
                            person = new JSONObject(responseData);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (person.getString("status").equals("error")) {
                                Toast.makeText(getContext(), "donation failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Donation added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }


                });
            }
        });


        }
}
