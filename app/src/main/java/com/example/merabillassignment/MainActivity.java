package com.example.merabillassignment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {
    private TextView totalAmountTextView,paymentsLabelTextView;
    private LinearLayout paymentContainer;
    private List<Payment> paymentList;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        paymentsLabelTextView = findViewById(R.id.payments_label);
        paymentContainer = findViewById(R.id.paymentContainer);
        paymentList = new ArrayList<>();
        saveButton = findViewById(R.id.saveButton);

        // Load payment details from file (if available)
        loadPaymentDetails();

        findViewById(R.id.addPaymentLink).setOnClickListener(v -> showAddPaymentDialog());

        updateTotalAmount();
        displayPayments();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_payment_validation_msg, Toast.LENGTH_SHORT).show();
                } else {
                    // Check if any payment has an empty amount
                    boolean hasEmptyAmount = false;
                    for (Payment payment : paymentList) {
                        if (payment.getAmount() <= 0) {
                            hasEmptyAmount = true;
                            break;
                        }
                    }

                    if (hasEmptyAmount) {
                        Toast.makeText(getApplicationContext(), R.string.wrong_payment_validation_msg, Toast.LENGTH_SHORT).show();
                    } else {
                        savePaymentDetails();
                        Toast.makeText(getApplicationContext(), R.string.payment_saved, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadPaymentDetails() {
        String json = FileHelper.readFile(getApplicationContext(), "LastPayment.txt");
        if (json != null && !json.isEmpty()) {
            Gson gson = new Gson();
            paymentList = gson.fromJson(json, new TypeToken<List<Payment>>() {}.getType());
        }
    }

    private void showAddPaymentDialog() {
        if(getAvailablePaymentTypes().size()==0){
            Toast.makeText(this, R.string.payment_chips_full_msg, Toast.LENGTH_SHORT).show();
        }else{
            AddPaymentDialog dialog = new AddPaymentDialog();
            dialog.setPaymentTypes(getAvailablePaymentTypes());
            dialog.setOnPaymentAddedListener(this::addPayment);
            dialog.show(getSupportFragmentManager(), "add_payment_dialog");
        }
    }

    private List<String> getAvailablePaymentTypes() {
        List<String> availableTypes = new ArrayList<>();
        if (!isPaymentTypeAdded(PaymentType.CASH)) {
            availableTypes.add(PaymentType.CASH);
        }
        if (!isPaymentTypeAdded(PaymentType.BANK_TRANSFER)) {
            availableTypes.add(PaymentType.BANK_TRANSFER);
        }
        if (!isPaymentTypeAdded(PaymentType.CREDIT_CARD)) {
            availableTypes.add(PaymentType.CREDIT_CARD);
        }
        return availableTypes;
    }

    private boolean isPaymentTypeAdded(String paymentType) {
        for (Payment payment : paymentList) {
            if (payment.getPaymentType().equals(paymentType)) {
                return true;
            }
        }
        return false;
    }

    private void addPayment(Payment payment) {
        paymentList.add(payment);
        updateTotalAmount();
        displayPayments();
    }

    private void updateTotalAmount() {
        double totalAmount = 0;
        for (Payment payment : paymentList) {
            totalAmount += payment.getAmount();
        }
        if(totalAmount>0) {
            totalAmountTextView.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        }else{
            totalAmountTextView.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String labelText = "Total Amount: ₹";
        builder.append(labelText);
        int start = builder.length();
        String valueText = String.format(Locale.getDefault(), " %.2f", totalAmount);
        builder.append(valueText);
        // Apply bold style to the value text
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, start + valueText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalAmountTextView.setText((builder));
    }

    private void displayPayments() {
        if(paymentList.size()>0){
            paymentContainer.setVisibility(View.VISIBLE);
            paymentsLabelTextView.setVisibility(View.VISIBLE);
        }else{
            paymentContainer.setVisibility(View.GONE);
            paymentsLabelTextView.setVisibility(View.GONE);
        }
        paymentContainer.removeAllViews();
        for (final Payment payment : paymentList) {
            Chip paymentChip = (Chip) LayoutInflater.from(this).inflate(R.layout.payment_chip, paymentContainer, false);
            paymentChip.setText(payment.getDisplayText());
            paymentChip.setOnCloseIconClickListener(v -> removePayment(payment));
            paymentContainer.addView(paymentChip);
        }
    }

    private void removePayment(Payment payment) {
        paymentList.remove(payment);
        updateTotalAmount();
        displayPayments();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void savePaymentDetails() {
        Gson gson = new Gson();
        String json = gson.toJson(paymentList);
        FileHelper.writeFile(getApplicationContext(), "LastPayment.txt", json);
    }
}
