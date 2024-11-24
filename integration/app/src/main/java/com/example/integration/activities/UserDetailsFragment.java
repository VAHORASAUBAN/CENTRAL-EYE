package com.example.integration.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.integration.R;

public class UserDetailsFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_MOBILE = "mobile";
    private static final String ARG_GENDER = "gender";
    private static final String ARG_ROLE = "role";
    private static final String ARG_STATION = "station";

    private String name = "N/A";
    private String mobile = "N/A";
    private String gender = "N/A";
    private String role = "N/A";
    private String station = "N/A";

    /**
     * Creates a new instance of the UserDetailsFragment.
     *
     * @param name     The full name of the user.
     * @param mobile   The contact number of the user.
     * @param gender   The gender of the user.
     * @param role     The role of the user.
     * @param station  The station associated with the user.
     * @return A new instance of UserDetailsFragment.
     */
    public static UserDetailsFragment newInstance(String name, String mobile, String gender, String role, String station) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_MOBILE, mobile);
        args.putString(ARG_GENDER, gender);
        args.putString(ARG_ROLE, role);
        args.putString(ARG_STATION, station);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Extract arguments if available
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME, "N/A");
            mobile = getArguments().getString(ARG_MOBILE, "N/A");
            gender = getArguments().getString(ARG_GENDER, "N/A"); // Handle null gracefully
            role = getArguments().getString(ARG_ROLE, "N/A");
            station = getArguments().getString(ARG_STATION, "N/A");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        // Views from the XML layout
        ImageView profileImage = view.findViewById(R.id.userProfileImage);
        TextView nameText = view.findViewById(R.id.fullName);
        EditText mobileText = view.findViewById(R.id.mobileNumber);
        RadioGroup genderGroup = view.findViewById(R.id.genderGroup);
        EditText roleText = view.findViewById(R.id.role);
        EditText stationText = view.findViewById(R.id.station);

        // Populate user details into the views
        nameText.setText(name);
        mobileText.setText(mobile);
        roleText.setText(role);
        stationText.setText(station);

        // Handle gender (since the user cannot change this, set the gender)
        if ("Male".equalsIgnoreCase(gender)) {
            RadioButton maleRadioButton = view.findViewById(R.id.genderMale);
            maleRadioButton.setChecked(true);
        } else if ("Female".equalsIgnoreCase(gender)) {
            RadioButton femaleRadioButton = view.findViewById(R.id.genderFemale);
            femaleRadioButton.setChecked(true);
        }

        // Optionally set a default profile image or dynamically load an image.
        profileImage.setImageResource(R.drawable.profile); // Placeholder profile image

        // Disable the EditTexts to prevent changes
        mobileText.setEnabled(false);
        roleText.setEnabled(false);
        stationText.setEnabled(false);

        return view;
    }
}
