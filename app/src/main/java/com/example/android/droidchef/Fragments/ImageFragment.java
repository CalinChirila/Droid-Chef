package com.example.android.droidchef.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.droidchef.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Astraeus on 1/24/2018.
 */

public class ImageFragment extends Fragment {

    private String mThumbnail;

    public ImageFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = rootView.findViewById(R.id.iv_image_fragment);

        if(mThumbnail != null && !TextUtils.isEmpty(mThumbnail)) {
            Picasso.with(rootView.getContext())
                    .load(mThumbnail)
                    .placeholder(getResources().getDrawable(R.drawable.chefhat))
                    .error(getResources().getDrawable(R.drawable.chefhat))
                    .into(imageView);
        } else {
            Picasso.with(rootView.getContext())
                    .load(R.drawable.chefhat)
                    .into(imageView);
        }
        return rootView;
    }

    public void setImageThumbnail(String thumbnail){
        mThumbnail = thumbnail;
    }
}
