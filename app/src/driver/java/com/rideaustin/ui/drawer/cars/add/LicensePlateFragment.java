package com.rideaustin.ui.drawer.cars.add;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rideaustin.R;
import com.rideaustin.databinding.LicensePlateBinding;
import com.rideaustin.stub.SimpleTextWatcher;
import com.rideaustin.ui.utils.MenuUtil;
import com.rideaustin.utils.KeyboardUtil;

import java.util.Arrays;

public class LicensePlateFragment extends BaseAddCarFragment {

    private MenuItem nextMenuItem;
    private LicensePlateBinding licensePlateBinding;

    public static LicensePlateFragment newInstance(AddCarActivity.AddCarSequence sequence) {
        LicensePlateFragment fragment = new LicensePlateFragment();
        Bundle args = new Bundle();
        args.putSerializable(SEQUENCE_KEY, sequence);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        licensePlateBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_license_plate, container, false);
        setToolbarTitle(R.string.title_license_plate);
        setHasOptionsMenu(true);
        licensePlateBinding.licensePlaceInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (nextMenuItem != null) {
                    nextMenuItem.setEnabled(validateLicensePlateInputField());
                }
            }
        });

        InputFilter[] filters = licensePlateBinding.licensePlaceInput.getFilters();
        filters = Arrays.copyOf(filters, filters.length + 1);
        filters[filters.length - 1] = new InputFilter.AllCaps();
        licensePlateBinding.licensePlaceInput.setFilters(filters);

        licensePlateBinding.licensePlaceInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                KeyboardUtil.showKeyBoard(getContext(), v);
            } else {
                KeyboardUtil.hideKeyBoard(getContext(), v);
            }
        });

        licensePlateBinding.licensePlaceInput.setText(addCarViewModel.getCarRegistration().getLicense());

        return licensePlateBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardUtil.showKeyBoard(getContext(), licensePlateBinding.licensePlaceInput);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        nextMenuItem = MenuUtil.inflateNextMenu(menu, inflater, false);
        nextMenuItem.setEnabled(validateLicensePlateInputField());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next:
                setLicenseToTheCar();
                notifyCompleted();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateLicensePlateInputField() {
        String value = licensePlateBinding.licensePlaceInput.getText().toString();
        return value.length() > 5;
    }


    private void setLicenseToTheCar() {
        addCarViewModel.getCarRegistration().setLicense(licensePlateBinding.licensePlaceInput.getText().toString());
    }
}
