package com.lisen.android.weixin6_0.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/15.
 */
public class TabFragment extends Fragment {

    private String mTitle = "";
    public final static String TITLE = "title";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTitle = getArguments().getString(TITLE);
        TextView textView = new TextView(getActivity());
        textView.setBackgroundColor(Color.parseColor("#ffffffff"));
        textView.setTextSize(20);
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
