package com.example.qrcodescanner.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.qrcodescanner.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyDetailsHolder extends RecyclerView.ViewHolder {
    TextView dateTv,remarkTv;
    public MyDetailsHolder(@NonNull View itemView) {
        super(itemView);
        this.dateTv = itemView.findViewById(R.id.dateBatch);
        this.remarkTv = itemView.findViewById(R.id.remarkBatch);
    }
}
