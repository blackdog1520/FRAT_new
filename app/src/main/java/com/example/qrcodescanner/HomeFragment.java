package com.example.qrcodescanner;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrcodescanner.adapters.HistoryAdapter;
import com.example.qrcodescanner.models.HistoryItems;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    List<HistoryItems> hisList = new ArrayList<>();
    HistoryAdapter myadapter;
    RecyclerView myRecView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        myRecView = view.findViewById(R.id.recViewHist);
        SharedPreferences newPref = getActivity().getApplicationContext().getSharedPreferences("HistoryPref", Context.MODE_PRIVATE);
        int total = newPref.getInt("usersHist",0);
        String teacher,subject,mail;
        for(int i =0;i<total;i++){
            teacher = newPref.getString("TeacherName"+i,"");
            subject = newPref.getString("batch"+i,"");
            mail = newPref.getString("mail"+i,"");
            hisList.add(new HistoryItems(teacher,subject,mail));
        }
        myadapter = new HistoryAdapter(hisList,getContext(),view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
        myRecView.setLayoutManager(gridLayoutManager);
        myRecView.setAdapter(myadapter);


        return view;
    }

}
