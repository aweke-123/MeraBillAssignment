package com.example.merabillassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private int spinnerItemLayoutResId;
    private List<String> spinnerItems;

    public CustomSpinnerAdapter(Context context, int spinnerItemLayoutResId, List<String> spinnerItems) {
        super(context, spinnerItemLayoutResId, spinnerItems);
        this.spinnerItemLayoutResId = spinnerItemLayoutResId;
        this.spinnerItems = spinnerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(spinnerItemLayoutResId, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinner_text);
        ImageView imageView = convertView.findViewById(R.id.spinner_icon);

        String spinnerItem = spinnerItems.get(position);
        textView.setText(spinnerItem);

        // Set the visibility of the dropdown icon to "GONE" in the selected item view
        imageView.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(spinnerItemLayoutResId, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinner_text);
        ImageView imageView = convertView.findViewById(R.id.spinner_icon);

        String spinnerItem = spinnerItems.get(position);
        textView.setText(spinnerItem);

        // Set the visibility of the dropdown icon to "VISIBLE" in the dropdown item view
        imageView.setVisibility(View.GONE);

        return convertView;
    }
}
