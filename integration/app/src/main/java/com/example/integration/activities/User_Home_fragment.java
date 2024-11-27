package com.example.integration.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.integration.R;

public class User_Home_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public User_Home_fragment() {
        // Required empty public constructor
    }

    public static User_Home_fragment newInstance(String param1, String param2) {
        User_Home_fragment fragment = new User_Home_fragment();
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
        View view = inflater.inflate(R.layout.fragment_user__home_fragment, container, false);

        // Fetch the username from shared preferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");

        // Bind views
//        TextView welcomeTextView = view.findViewById(R.id.headtext);
        ImageView profileImageButton = view.findViewById(R.id.profile_image);
        CardView squareBox1 = view.findViewById(R.id.squareBox1);
        CardView squareBox2 = view.findViewById(R.id.squareBox2);

        // Set welcome message
//        welcomeTextView.setText(username + "!");

        // Apply entrance animation to squareBox1 and squareBox2
        animateEntrance(squareBox1, 100);
        animateEntrance(squareBox2, 200);

        // Profile dropdown menu
        profileImageButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), profileImageButton);
            popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id= item.getItemId();
                if (id == R.id.menu_profile) {
                    Toast.makeText(requireContext(), "Profile Selected", Toast.LENGTH_SHORT).show();
                    openuserprofileFragment();
                    return true;
                } else if (id == R.id.menu_update_password) {
                    Toast.makeText(requireContext(), "Update Password Selected", Toast.LENGTH_SHORT).show();
                    openUpdatePasswordFragment();
                    return true;
                } else if (id == R.id.menu_logout) {
                    performLogout();
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });

        // Add click listener to squareBox1
        squareBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                            openProductListFragment();
                        })
                        .start();
            }
        });

        // Add click listener to squareBox2
        squareBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
//                            openUserListAdd();
                        })
                        .start();
            }
        });

        return view;
    }

    private void animateEntrance(View view, long delay) {
        view.setAlpha(0f);
        view.setTranslationY(50f); // Slide up effect
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(delay)
                .start();
    }

    private void performLogout() {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit();
        editor.clear(); // Clear session
        editor.apply();

        Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();

        // Navigate to login screen (optional)
        // Intent intent = new Intent(requireContext(), LoginActivity.class);
        // startActivity(intent);
        // requireActivity().finish();
    }

    private void openuserprofileFragment() {
        User_Profile_fragment userprofileFragment = new User_Profile_fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, userprofileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openUpdatePasswordFragment() {
//        UpdatePasswordFragment updatePasswordFragment = new UpdatePasswordFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, updatePasswordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    private void openProductListFragment() {
        ProductListFragment productListFragment = new ProductListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, productListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

   
}
