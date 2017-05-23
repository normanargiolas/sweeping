package it.namron.sweeping.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import it.namron.sweeping.model.AppItemModel;
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
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = makeLogTag(BaseActivity.class);

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private NavigationView mNavigationView;
    static private List<AppItemModel> mAppItemModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addDrawerItem(List<AppItemModel> appItemModelList) {
        if (appItemModelList != null){
            mAppItemModelList = appItemModelList;
            Menu menu = mNavigationView.getMenu();
            for (AppItemModel appItemModel : mAppItemModelList) {
                //params: groupId, itemId, order, title
                MenuItem item = menu.add(R.id.homegroup_menu, appItemModel.getId(), Menu.NONE, appItemModel.getAppName());
                item.setIcon(appItemModel.getAppIcon());
            }
        }
    }

    public AppItemModel getAppItemById(int id){
        if(mAppItemModelList != null && mAppItemModelList.size()>= id)
            return mAppItemModelList.get(id);
        else
            return null;
    }


    public void setDrawer(Context context) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);

        // adding nav drawer items
        addDrawerItem(mAppItemModelList);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        LOGD(TAG, "onNavigationItemSelected");
        return false;
    }
}
