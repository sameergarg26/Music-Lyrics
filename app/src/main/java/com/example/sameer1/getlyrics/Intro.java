package com.example.sameer1.getlyrics;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Intro extends AppCompatActivity {
    int count =3;
    ViewPager pager;
    TextView[] view = new TextView[3];


    String unselected = ".";
    String selected = ".";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        view[0] = (TextView) findViewById(R.id.view1);
        view[1] = (TextView) findViewById(R.id.view2);
        view[2] = (TextView) findViewById(R.id.view3);

        view[0].setTextColor(Color.parseColor("#fff123"));
        view[1].setTextColor(Color.parseColor("#ffffff"));
        view[2].setTextColor(Color.parseColor("#ffffff"));

        Intent intent = getIntent();

        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                view[0].setTextColor(Color.parseColor("#ffffff"));
                view[1].setTextColor(Color.parseColor("#ffffff"));
                view[2].setTextColor(Color.parseColor("#ffffff"));


                view[position].setTextColor(Color.parseColor("#fff123"));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: Fragment fm1= new IntroFragmentOne();
                    return fm1;
                case 1: Fragment fm2= new IntroFragmentTwo();
                    return fm2;
                default: Fragment fm3= new IntroFragmentThree();
                    return fm3;
            }
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
