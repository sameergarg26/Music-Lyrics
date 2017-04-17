package com.example.sameer1.getlyrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.sameer1.getlyrics.R.id.toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar tb;
    DrawerLayout myDrawer;
    NavigationView navigation;
    FloatingActionButton fab;
    ActionBar ab;

    public static int navItemIndex = 0;

    private static final String TAG_GET_LYRICS = "Get Lyrics";
    private static final String TAG_SAVED_LYRICS = "Saved Lyrics";
    private static final String TAG_ABOUT = "About App";
    public static String CURRENT_TAG = TAG_GET_LYRICS;

    private String[] activityTitles;



    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Music Lyrics");
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu_white);

        fab = (FloatingActionButton) findViewById(R.id.refresh);
        myDrawer = (DrawerLayout) findViewById(R.id.activity_main);
        navigation = (NavigationView) findViewById(R.id.my_navigation_view);
        activityTitles = new String[3];
        activityTitles[0] = TAG_GET_LYRICS;
        activityTitles[1] = TAG_SAVED_LYRICS;
        activityTitles[2] = TAG_ABOUT;

        // initializing navigation menu
        setUpNavigationView();

        if(savedInstanceState == null){
            navItemIndex = 0;
        }

        loadHomeFragment();

    }

    private void loadHomeFragment() {

        // set toolbar title

        // selecting appropriate nav menu item
        selectNavMenu();


        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            myDrawer.closeDrawers();
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                       android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        // show or hide the fab button
        toggleFab();
        //Closing drawer on item click
        myDrawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                GetLyrics getLyrics = new GetLyrics();
                return getLyrics;
            case 1:
                // photos
                SavedLyrics savedLyrics = new SavedLyrics();
                return savedLyrics;
            default:
                return new GetLyrics();
        }
    }

    private void setToolbarTitle() {
        //Log.d("Value",activityTitles[navItemIndex]);
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigation.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_get_lyrics:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_GET_LYRICS;
                        break;
                    case R.id.nav_saved_lyrics:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SAVED_LYRICS;
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutApp.class));
                        myDrawer.closeDrawers();
                        return true;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, myDrawer, tb, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        myDrawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (myDrawer.isDrawerOpen(GravityCompat.START)) {
            myDrawer.closeDrawers();
            return;
        }

        /*
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_GET_LYRICS;
                loadHomeFragment();
                return;
            }
        }
*/
        super.onBackPressed();
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    public FloatingActionButton getButton(){
        return fab;
    }

}
