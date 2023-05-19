package com.example.merabillassignment;

public class Payment {
    private String paymentType;
    private double amount;
    private String provider;
    private String transactionReference;

    public Payment(String paymentType, double amount, String provider, String transactionReference) {
        this.paymentType = paymentType;
        this.amount = amount;
        this.provider = provider;
        this.transactionReference = transactionReference;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public double getAmount() {
        return amount;
    }

    public String getProvider() {
        return provider;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public String getDisplayText() {
        String displayText = paymentType.toString() + " - ₹" + amount;
        if (provider != null && !provider.isEmpty()) {
            displayText += " (" + provider + ")";
        }
        if (transactionReference != null && !transactionReference.isEmpty()) {
            displayText += " [" + transactionReference + "]";
        }
        return displayText;
    }
}