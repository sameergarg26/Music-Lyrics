package com.example.sameer1.getlyrics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class IntroFragmentThree extends Fragment {
    TextView t2, gs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_intro_fragment_three, container, false);
        t2 = (TextView) v.findViewById(R.id.textExp);
        gs = (TextView) v.findViewById(R.id.getstarted);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/mary.ttf");
        t2.setTypeface(tf);

        gs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                //getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
            }
        });

        return v;
    }

}
