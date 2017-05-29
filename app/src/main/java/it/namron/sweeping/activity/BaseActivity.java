package it.namron.sweeping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import it.namron.core.utility.WrappedDrawable;
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

    private int mLayout;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawer;

    private NavigationView mNavigationView;
    static private List<AppItemModel> mAppItemModelList;
//    private Context mContext;

    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);
            mSavedInstanceState = savedInstanceState;
        }
    }

    public void setLayout(int layout) {
        mLayout = layout;
    }

    public void addDrawerItem(List<AppItemModel> appItemModelList) {
        if (appItemModelList != null) {
//            if (mSavedInstanceState == null) {

            mAppItemModelList = appItemModelList;
//            mNavigationView.getMenu().clear();
            Menu menu = mNavigationView.getMenu();
            for (AppItemModel appItemModel : mAppItemModelList) {
                //params: groupId, itemId, order, title
                MenuItem item = menu.add(R.id.homegroup_menu, appItemModel.getId(), Menu.NONE, appItemModel.getAppName());
//
//                WrappedDrawable wrappedDrawable = new WrappedDrawable(appItemModel.getAppIcon());
//                wrappedDrawable.setBounds(0, 0, 10, 32);
//                Drawable drawable = new ScaleDrawable(appItemModel.getAppIcon(), 0, 40, 40).getDrawable();
//                drawable.setBounds(0, 0, 40, 40);
//                Bitmap bitmap = ((BitmapDrawable) appItemModel.getAppIcon()).getBitmap();
//                Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 24, 24, true));
//                item.setIcon(drawable);

                item.setIcon(appItemModel.getAppIcon());
                item.setTitle(appItemModel.getAppName());
            }


//            }


            // Recycle the typed array
//            mNavigationView.refreshDrawableState();
//            mDrawer.refreshDrawableState();
//            mToggle.syncState();
        }
    }

    public AppItemModel getAppItemById(int id) {
        if (mAppItemModelList != null && mAppItemModelList.size() >= id)
            return mAppItemModelList.get(id);
        else
            return null;
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mToggle.syncState();
//    }


    public void setDrawer(Context context) {

//            mContext = context;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        mDrawer.refreshDrawableState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);


        // adding nav mDrawer items
//        addDrawerItem(mAppItemModelList);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        LOGD(TAG, "onNavigationItemSelected");
        return false;
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

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
//        LOGD(TAG, "onRestoreInstanceState");
//
//    }

//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        NavigationView.SavedState state = new NavigationView.SavedState(superState);
//        state.menuState = new Bundle();
//        this.mMenu.savePresenterStates(state.menuState);
//        return state;
//    }

}
