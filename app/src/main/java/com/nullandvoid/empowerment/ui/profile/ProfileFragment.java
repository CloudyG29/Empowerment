package com.nullandvoid.empowerment.ui.profile;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.nullandvoid.empowerment.DonationActivity;
import com.nullandvoid.empowerment.LoginActivity;
import com.nullandvoid.empowerment.NotificationsActivity;
import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.RequestsActivity;
import com.nullandvoid.empowerment.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String NAME_KEY = "name_key";
    public static final String USER_KEY = "user_key";
    public static final String SURNAME_KEY = "surname_key";
    SharedPreferences sharedPreferences;
    String userid, email, name, surname;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        userid = sharedPreferences.getString(USER_KEY, null);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        name = sharedPreferences.getString(NAME_KEY, null);
        surname = sharedPreferences.getString(SURNAME_KEY, null);

        TextView t_name = root.findViewById(R.id.profile_name);
        TextView t_email = root.findViewById(R.id.profile_email);
        t_name.setText(name);
        t_email.setText(email);

        View.OnClickListener copy = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData txt = ClipData.newPlainText("Email", t_email.getText());
                clipboard.setPrimaryClip(txt);
                Toast.makeText(requireContext(), "Email copied!", Toast.LENGTH_SHORT).show();
            }
        };
        t_email.setOnClickListener(copy);


        ImageView notification_img = root.findViewById(R.id.notification_bar);
        ImageView donation_img = root.findViewById(R.id.donation_bar);
        ImageView request_img = root.findViewById(R.id.request_bar);
        ImageView logout_img = root.findViewById(R.id.logout_bar);
        //FragmentContainerView nestedHost = root.findViewById(R.id.nested_nav_host);




        View.OnClickListener notificationClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to Notifications
                Intent intent = new Intent(getContext(), NotificationsActivity.class);
                startActivity(intent);
                //requireActivity().finish();
            }
        };
        View.OnClickListener donationsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to donations
                Intent intent = new Intent(getContext(), DonationActivity.class);
                startActivity(intent);
                //requireActivity().finish();
            }
        };
        View.OnClickListener requestsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to requests
                Intent intent = new Intent(getContext(), RequestsActivity.class);
                startActivity(intent);
                //requireActivity().finish();

            }
        };
        View.OnClickListener logoutClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Do you want to logout?");

                builder.setTitle("Leaving?");

                builder.setCancelable(true);


                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    requireActivity().finish();

                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

        notification_img.setOnClickListener(notificationClickListener);
        donation_img.setOnClickListener(donationsClickListener);
        request_img.setOnClickListener(requestsClickListener);
        logout_img.setOnClickListener(logoutClickListener);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}