package it.namron.sweeping.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.fragment.TelegramFragment;
import it.namron.sweeping.fragment.WhatsAppFragment;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.LogUtils.LOGD;
import static it.namron.sweeping.utils.LogUtils.makeLogTag;

/**
 * Created by norman on 20/05/17.
 */

/**
 * A base activity that handles common functionality in the app. This includes the navigation
 * drawer and in the future login and authentication, Action Bar tweaks, amongst others.
 */
public class BaseActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = makeLogTag(BaseActivity.class);

//    private DrawerLayout mDrawerLayout;
//    private ListView mDrawerList;
//    private ActionBarDrawerToggle mDrawerToggle;
//    protected RelativeLayout _completeLayout, _activityLayout;
    // nav drawer title
//    private CharSequence mDrawerTitle;
//    private Menu menuObject;
    // used to store app title
//    private CharSequence mTitle;
//    Toolbar toolbar;
//    private ArrayList<NavDrawerItem> navDrawerItems;
//    private NavDrawerListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // R.id.drawer_layout should be in every activity with exactly the same id.
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_tmp);
    }

    public void set(String[] navMenuTitles, TypedArray navMenuIcons, Context context) {
//        mTitle = mDrawerTitle = getTitle();

//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        navDrawerItems = new ArrayList<NavDrawerItem>();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        // adding nav drawer items
//        if (navMenuIcons == null) {
//            for (int i = 0; i < navMenuTitles.length; i++) {
//                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
//            }
//        } else {
//            for (int i = 0; i < navMenuTitles.length; i++) {
//                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
//                        navMenuIcons.getResourceId(i, -1)));
//            }
//        }

//        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        // setting the nav drawer list adapter
//        adapter = new NavDrawerListAdapter(getApplicationContext(),
//                navDrawerItems);





//        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

//        setupDrawerToggle();

        // getSupportActionBar().setIcon(R.drawable.ic_drawer);
        //mDrawerToggle.syncState();

//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                toolbar, // nav menu toolbar instead of icon
//                R.string.menu, // nav drawer open - description for accessibility
//                R.string.app_name // nav drawer close - description for accessibility
//        ) {
//            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(mTitle);
//                // calling onPrepareOptionsMenu() to show action bar icons
//                supportInvalidateOptionsMenu();
//                //mDrawerToggle.syncState();
//            }
//
//            public void onDrawerOpened(View drawerView) {
////                getSupportActionBar().setTitle(mDrawerTitle);
//                // calling onPrepareOptionsMenu() to hide action bar icons
//                supportInvalidateOptionsMenu();
//                //mDrawerToggle.syncState();
//            }
//        };
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

//
//    void setupDrawerToggle() {
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
//        //This is necessary to change the icon of the Drawer Toggle upon state change.
//        mDrawerToggle.syncState();
//    }


    //start new activity
    //    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_whatsapp:
                this.setTitle("WhatsApp");
                Toast.makeText(getApplicationContext(), "whatsapp", Toast.LENGTH_SHORT).show();
                fragment = new WhatsAppFragment();
                break;
            case R.id.nav_telegram:
                this.setTitle("Telegram");
                Toast.makeText(getApplicationContext(), "telegram", Toast.LENGTH_SHORT).show();
                fragment = new TelegramFragment();
                break;
            case R.id.nav_manage:
                this.setTitle("Manage");
                Toast.makeText(getApplicationContext(), "manage", Toast.LENGTH_SHORT).show();
                fragment = new ManageFragment();
                break;
            default:
                Toast.makeText(getApplicationContext(), "unknow choice", Toast.LENGTH_SHORT).show();

                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    private class SlideMenuClickListener implements
//            ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//                                long id) {
//            // display view for selected nav drawer item
//            displayView(position);
//        }
//    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
//    private void displayView(int position) {
//
//        switch (position) {
//            case 0:
//                Intent intent = new Intent(this, AppInfoActivity.class);
//                startActivity(intent);
//                finish();// finishes the current activity
//                break;
////            case 1:
////                Intent intent1 = new Intent(this, SecondActivity.class);
////                startActivity(intent1);
////                finish();// finishes the current activity
////                break;
//            //case 2:
//            //  Intent intent2 = new Intent(this, ThirdActivity.class);
//            //  startActivity(intent2);
//            //  finish();
//            //  break;
//            // case 3:
//            // Intent intent3 = new Intent(this, fourth.class);
//            // startActivity(intent3);
//            // finish();
//            // break;
//            // case 4:
//            // Intent intent4 = new Intent(this, fifth.class);
//            // startActivity(intent4);
//            // finish();
//            // break;
//            // case 5:
//            // Intent intent5 = new Intent(this, sixth.class);
//            // startActivity(intent5);
//            // finish();
//            // break;
//            default:
//                break;
//        }
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        mDrawerList.setSelection(position);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LOGD(TAG, "onPostCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LOGD(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LOGD(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LOGD(TAG, "onDestroy");
    }

}
