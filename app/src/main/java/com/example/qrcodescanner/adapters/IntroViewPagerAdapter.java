package com.example.qrcodescanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.qrcodescanner.R;
import com.example.qrcodescanner.models.ScreenItems;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context mcontext;

    public IntroViewPagerAdapter(Context mcontext, List<ScreenItems> mDataList) {
        this.mcontext = mcontext;
        this.mDataList = mDataList;
    }

    List<ScreenItems> mDataList;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,null);
       LottieAnimationView imageView = layoutScreen.findViewById(R.id.screenImage);
        TextView introView = layoutScreen.findViewById(R.id.introTextView);
        TextView descView = layoutScreen.findViewById(R.id.descTextView);

        imageView.setAnimation(mDataList.get(position).getLottieid());
        imageView.playAnimation();
        imageView.setRepeatCount(LottieDrawable.INFINITE);
        introView.setText(mDataList.get(position).getTitle());
        descView.setText(mDataList.get(position).getDescription());
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
