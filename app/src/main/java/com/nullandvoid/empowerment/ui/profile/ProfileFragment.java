package com.nullandvoid.empowerment.ui.profile;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nullandvoid.empowerment.LoginActivity;
import com.nullandvoid.empowerment.R;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        userid = sharedPreferences.getString(USER_KEY, null);
        email = sharedPreferences.getString(EMAIL_KEY, null);
        name = sharedPreferences.getString(NAME_KEY, null);
        surname = sharedPreferences.getString(SURNAME_KEY, null);

        ImageView img = root.findViewById(R.id.logout_img);
        TextView text = root.findViewById(R.id.text_logout);
        ImageView arrow = root.findViewById(R.id.logout_arr);

        System.out.println(name);
        System.out.println(name);
        System.out.println(name);
        System.out.println(surname);

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

        View.OnClickListener logoutClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.clear();
               editor.apply();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        };

        img.setOnClickListener(logoutClickListener);
        text.setOnClickListener(logoutClickListener);
        arrow.setOnClickListener(logoutClickListener);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}