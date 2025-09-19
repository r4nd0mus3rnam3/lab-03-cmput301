package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city, String newName, String newProvince);
    }
    private EditText editCityName;
    private EditText editProvinceName;
    private AddCityDialogListener listener;
    private City existingCity;

    private static final String ARG_CITY = "city_to_edit";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city); // City needs to be Serializable
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_city, null);
        editCityName = view.findViewById(R.id.edit_text_city_text); // Make sure you have these IDs in your layout
        editProvinceName = view.findViewById(R.id.edit_text_province_text); // Make sure you have these IDs in your layout

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Check if a City object was passed for editing
        if (getArguments() != null && getArguments().containsKey(ARG_CITY)) {
            existingCity = (City) getArguments().getSerializable(ARG_CITY);
            if (existingCity != null) {
                editCityName.setText(existingCity.getName());
                editProvinceName.setText(existingCity.getProvince());
                builder.setTitle("Edit City"); // Change dialog title
            }
        } else {
            builder.setTitle("Add City"); // Default title
        }


        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(existingCity != null ? "Save" : "Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();

                        if (!cityName.isEmpty() && !provinceName.isEmpty()) {
                            if (existingCity != null) {
                                // We are editing an existing city
                                listener.editCity(existingCity, cityName, provinceName);
                            } else {
                                // We are adding a new city
                                listener.addCity(new City(cityName, provinceName));
                            }
                        }
                    }
                })
                .create();
    }}
