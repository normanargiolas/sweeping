package it.namron.sweeping.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.adapter.DrawerItemAdapter;
import it.namron.sweeping.dto.AppItemDTO;
import it.namron.sweeping.dto.DrawerItemDTO;
import it.namron.sweeping.exception.ExceptionHandler;
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
public abstract class BaseActivity extends AppCompatActivity implements DrawerItemAdapter.DrawerItemAdapterOnClickListener {

    private static final String TAG = makeLogTag(BaseActivity.class);

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private int mLayout;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawer;

    private DrawerItemAdapter mDrawerEntryAdapter;
    static private List<DrawerItemDTO> mDrawerListModel;

    //    private NavigationView mNavigationView;
    static private List<AppItemDTO> mAppItemModelList;
//    private Context mContext;

    private RecyclerView mDrawerRecyclerView;

    private Bundle mSavedInstanceState;

    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *Attach our Exception handler in base activity
         * in order to have each activity with this ExceptionHandler
         */
        //todo decommentare in futuro
//        new ExceptionHandler(BaseActivity.this);


        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);
            mSavedInstanceState = savedInstanceState;
        }
    }

    public void setLayout(int layout) {
        mLayout = layout;
    }


    public void addDrawerItemFromAppList(List<AppItemDTO> appItemModelList) {
        if (appItemModelList != null) {
            mAppItemModelList = appItemModelList;
            mDrawerListModel = getDrawerFromApp(appItemModelList);
        }
        if (mDrawerEntryAdapter != null && mDrawerListModel != null) {
            mDrawerEntryAdapter.updateDrawer(mDrawerListModel);
        }
    }

    private List<DrawerItemDTO> getDrawerFromApp(List<AppItemDTO> mAppListModel) {
        List<DrawerItemDTO> drawerItemList = new ArrayList<>();
        for (AppItemDTO appItem : mAppListModel) {
            DrawerItemDTO drawerItem = new DrawerItemDTO();
            drawerItem.setDrawerName(appItem.getAppName());
            drawerItem.setDrawerIcon(appItem.getAppIcon());
            drawerItem.setId(appItem.getId());
            drawerItemList.add(drawerItem);
        }
        return drawerItemList;
    }

    public AppItemDTO getAppItemByDrawerId(int id) {
        if (mAppItemModelList != null && mAppItemModelList.size() >= id)
            return mAppItemModelList.get(id);
        else
            return null;
    }

    public void setDrawer(Context context) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        mDrawerRecyclerView = (RecyclerView) header.findViewById(R.id.drawer_list_recycler);
        RecyclerView.LayoutManager mDrawerLayoutManager = new LinearLayoutManager(getApplicationContext());
        mDrawerRecyclerView.setLayoutManager(mDrawerLayoutManager);
        mDrawerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDrawerRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mDrawerRecyclerView.setHasFixedSize(true);


        //The DrawerItemAdapter is responsible for displaying each item in the list.
        mDrawerEntryAdapter = new DrawerItemAdapter(this, mDrawerListModel, this);
        mDrawerRecyclerView.setAdapter(mDrawerEntryAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
////        menu.clear(); // Clear the menu first
//
//            /* Add the menu items */
//
//        return super.onPrepareOptionsMenu(menu);
//    }

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);

        LOGD(TAG, "onConfigurationChanged");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LOGD(TAG, "onSaveInstanceState");
    }


    /**
     * The interface that receives onClick messages from DrawerItemAdapter.
     */
    @Override
    public boolean onNavigationItemSelected(DrawerItemDTO item) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + item.getId() + " clicked.";
        mToast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);

        mToast.show();

        return true;
    }

}
