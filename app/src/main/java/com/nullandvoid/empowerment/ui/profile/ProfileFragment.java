package com.nullandvoid.empowerment.ui.profile;

import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.nullandvoid.empowerment.DonationActivity;
import com.nullandvoid.empowerment.LoginActivity;
import com.nullandvoid.empowerment.Menu;
import com.nullandvoid.empowerment.NotificationsActivity;
import com.nullandvoid.empowerment.R;
import com.nullandvoid.empowerment.RequestsActivity;
import com.nullandvoid.empowerment.databinding.FragmentProfileBinding;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String NAME_KEY = "name_key";
    public static final String USER_KEY = "user_key";
    public static final String SURNAME_KEY = "surname_key";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private CircleImageView profileImage;
    SharedPreferences sharedPreferences;
    String userid, email, name, surname;

    private FragmentProfileBinding binding;
    OkHttpClient client = new OkHttpClient();

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


        profileImage = root.findViewById(R.id.profile_image);

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        new Handler(Looper.getMainLooper()).post(() -> {
            String profileImagePath = sharedPreferences.getString("profile_image_path", null);
            if (profileImagePath != null) {
                Glide.with(requireContext())
                        .load(profileImagePath)
                        .placeholder(R.drawable.default_profile)
                        .thumbnail(0.1f) // tiny preview first
                        .into(profileImage);
            }

        });
        fetchProfileImageFromServer();
    Menu.hideProgressBar();
        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri sourceUri = result.getData().getData();

                    Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), "cropped.jpg"));

                    Activity activity = getActivity();
                    if (activity != null) {
                        UCrop.of(sourceUri, destinationUri)
                                .withAspectRatio(1, 1)
                                .withMaxResultSize(300, 300) // or 200x200 if you're bold
                                .withOptions(getCropOptions())
                                .start(requireContext(), ProfileFragment.this, UCrop.REQUEST_CROP);
                    }
                }
            }
    );
    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70); // Compress more aggressively
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        return options;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Menu.showProgressBar();
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri croppedUri = UCrop.getOutput(data);
            profileImage.setImageURI(croppedUri);
            File imageFile = new File(croppedUri.getPath());
            uploadImageToServer(imageFile);

            // TODO: upload croppedUri to server
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(getContext(), "Crop failed: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Menu.hideProgressBar();
    }

    public void fetchProfileImageFromServer() {
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", userid)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/get_profile_image.php") // 👈 Make this endpoint
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getString("status").equals("Success")) {
                            String path = json.getString("path");
                            String fullUrl = "https://lamp.ms.wits.ac.za/home/s2801257/" + path;

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("profile_image_path", fullUrl);
                            editor.apply();

                            requireActivity().runOnUiThread(() -> {
                                Glide.with(requireContext())
                                        .load(fullUrl)
                                        .signature(new ObjectKey(System.currentTimeMillis()))
                                        .placeholder(R.drawable.default_profile)
                                        .into(profileImage);
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("PROFILE_FETCH", "JSON parse error: " + e.getMessage());
                    }
                }


            }
        });
    }

    private void uploadImageToServer(File imageFile) {
        OkHttpClient client = new OkHttpClient();

        String mimeType = requireContext().getContentResolver().getType(Uri.fromFile(imageFile));
        RequestBody fileBody = RequestBody.create(imageFile, MediaType.parse(mimeType != null ? mimeType : "image/jpeg"));

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("profile_image", imageFile.getName(), fileBody)
                .addFormDataPart("user_id", userid) // important: send user ID
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2801257/upload_image.php")  // 👈 Your actual PHP endpoint here
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("UPLOAD_RESPONSE", responseBody);

                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            if (json.getString("status").equals("success")) {
                                String path = json.getString("path");
                                String fullUrl = "https://lamp.ms.wits.ac.za/home/s2801257/" + path;

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("profile_image_path", fullUrl);
                                editor.apply();

                                Glide.with(requireContext())
                                        .load(fullUrl)
                                        .placeholder(R.drawable.default_profile)
                                        .into(profileImage);

                                Toast.makeText(getContext(), "Image uploaded!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Upload failed: Server error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Upload failed: Invalid JSON", Toast.LENGTH_SHORT).show();
                            Log.e("UPLOAD_JSON", "JSON parsing error: " + e.getMessage());
                        }

                    } else {
                        Toast.makeText(getContext(), "Upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



}