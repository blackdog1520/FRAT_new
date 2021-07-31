package com.example.qrcodescanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrcodescanner.models.ModelDate;
import com.example.qrcodescanner.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyDetailsAdapter extends RecyclerView.Adapter<MyDetailsHolder> {
    Context context;
    List<ModelDate> modelDateList;

    public MyDetailsAdapter(Context context, List<ModelDate> modelDateList) {
        this.context = context;
        this.modelDateList = modelDateList;
    }

    @NonNull
    @Override
    public MyDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_detail,null);

        return new MyDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDetailsHolder holder, int position) {
        String remark = modelDateList.get(position).getRemark();
        if(remark.equals("Absent")){
            holder.remarkTv.setTextColor(Color.parseColor("#000000"));
            holder.remarkTv.setText(remark);
        }else{

        holder.remarkTv.setText(remark);}
        holder.dateTv.setText(modelDateList.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return modelDateList.size();
    }
}
