package com.example.sameer1.getlyrics;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class IntroFragmentOne extends Fragment {
    TextView text, t2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_intro_fragment_one, container, false);
        text = (TextView) v.findViewById(R.id.textExp);
        t2 = (TextView) v.findViewById(R.id.textExp2);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/mary.ttf");
        text.setTypeface(tf);
        t2.setTypeface(tf);
        return v;
    }


}
