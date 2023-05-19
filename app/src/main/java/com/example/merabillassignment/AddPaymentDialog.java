package com.example.merabillassignment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class AddPaymentDialog extends DialogFragment {
    private Spinner paymentTypeSpinner;
    private EditText amountEditText;
    private EditText providerEditText;
    private EditText transactionEditText;
    private Button saveButton, cancelButton;
    private List<String> paymentTypes;
    private OnPaymentAddedListener onPaymentAddedListener;

    public interface OnPaymentAddedListener {
        void onPaymentAdded(Payment payment);
    }

    public void setPaymentTypes(List<String> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public void setOnPaymentAddedListener(OnPaymentAddedListener listener) {
        this.onPaymentAddedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_payment, null);

        paymentTypeSpinner = dialogView.findViewById(R.id.paymentTypeSpinner);
        amountEditText = dialogView.findViewById(R.id.amountEditText);
        providerEditText = dialogView.findViewById(R.id.providerEditText);
        transactionEditText = dialogView.findViewById(R.id.transactionEditText);
        saveButton = dialogView.findViewById(R.id.okButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(requireContext(), R.layout.spinner_dropdown_icon, paymentTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypeSpinner.setAdapter(adapter);

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                if (selectedType.equals(PaymentType.BANK_TRANSFER) || selectedType.equals(PaymentType.CREDIT_CARD)) {
                    providerEditText.setVisibility(View.VISIBLE);
                    transactionEditText.setVisibility(View.VISIBLE);
                } else {
                    providerEditText.setVisibility(View.GONE);
                    transactionEditText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveButton.setOnClickListener(v -> {
            String selectedType = (String) paymentTypeSpinner.getSelectedItem();
            String amountString = amountEditText.getText().toString();
            if (amountString.isEmpty()) {
                Toast.makeText(requireContext(), R.string.empty_amount_validation_msg, Toast.LENGTH_SHORT).show();
            }else{
                double amount = Double.parseDouble(amountString);
                String provider = providerEditText.getText().toString();
                String transactionReference = transactionEditText.getText().toString();
                if (amount <= 0) {
                    Toast.makeText(requireContext(), R.string.wrong_amount_validation_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Payment payment = new Payment(selectedType, amount, provider, transactionReference);
                    onPaymentAddedListener.onPaymentAdded(payment);
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        builder.setView(dialogView);
        return builder.create();
    }
}
