package com.example.qrcodescanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qrcodescanner.models.HistoryItems;
import com.example.qrcodescanner.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<HistoryItems> mList;
    LayoutInflater inflater;
    View view1;
  private int idOfSelected = -1;

    public HistoryAdapter(List<HistoryItems> mList, Context mContext,View view1) {
        this.mList = mList;
        this.mContext = mContext;
        this.view1 = view1;

        this.inflater = LayoutInflater.from(mContext);
    }

    Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView teacherView,SubjectView;
        CardView cardView;
        LinearLayout linearLayout ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherView = itemView.findViewById(R.id.textViewHistory);
            SubjectView = itemView.findViewById(R.id.textViewHistorySubject);
            cardView = itemView.findViewById(R.id.cardViewHistory);
            linearLayout = itemView.findViewById(R.id.linearLayoutCard);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_history,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String name = mList.get(position).getName();
        final String subject = mList.get(position).getSubject();
        final String mail = mList.get(position).getMail();
        holder.teacherView.setText(name);

        holder.itemView.setBackgroundResource(R.drawable.cardviewback);
        holder.cardView.setCardBackgroundColor(Color.parseColor("#A5CAD2"));
        holder.cardView.setCardElevation(0);
        holder.linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.backg));
        holder.teacherView.setTextColor(Color.parseColor("#FFFFFF"));
        holder.SubjectView.setTextColor(Color.parseColor("#F1F1F1"));
        holder.SubjectView.setText(subject);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(idOfSelected!=-1){
                    if(idOfSelected==position){
                        idOfSelected=-1;
                    }
                    notifyItemChanged(idOfSelected);

                }

                holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                holder.cardView.setCardElevation(5);
                holder.linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.backgpink));
            holder.teacherView.setTextColor(Color.parseColor("#FF7A89"));
            holder.SubjectView.setTextColor(Color.parseColor("#FF7A89"));
                TextView teach = view1.findViewById(R.id.mailID);
                TextView sub = view1.findViewById(R.id.subjectID);
                teach.setText(mail);
                sub.setText(subject);

                idOfSelected = position;

            }
        });

    }



    @Override
    public int getItemCount() {
        return mList.size();
    }
}
