package com.example.qrcodescanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.qrcodescanner.R;
import com.example.qrcodescanner.ShowAttendanceDateActivity;
import com.example.qrcodescanner.ShowOverallActivity;
import com.example.qrcodescanner.models.Viewitems;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewViewPagerAdapter extends PagerAdapter {
    Context mcontext;

    public ViewViewPagerAdapter(Context mcontext, List<Viewitems> mDataList) {
        this.mcontext = mcontext;
        this.mDataList = mDataList;
    }

    List<Viewitems> mDataList;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen_view,null);
       LottieAnimationView imageView = layoutScreen.findViewById(R.id.screenImageView);
        TextView introView = layoutScreen.findViewById(R.id.introTextViewView);


        imageView.setAnimation(mDataList.get(position).getLottieid());
        imageView.playAnimation();
        imageView.setRepeatCount(LottieDrawable.INFINITE);
        introView.setText(mDataList.get(position).getTitle());
        layoutScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position){
                    case 0: mcontext.startActivity(new Intent(mcontext, ShowAttendanceDateActivity.class));
                    break;
                    case 1: mcontext.startActivity(new Intent(mcontext, ShowOverallActivity.class));
                    break;

                }

            }
        });
        container.addView(layoutScreen);
        return layoutScreen;




    }


    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }
}
