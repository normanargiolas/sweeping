package it.namron.sweeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Toast;

import it.namron.sweeping.dto.AppItemDTO;
import it.namron.sweeping.dto.DrawerItemDTO;
import it.namron.sweeping.fragment.AppInfoFragment;
import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.constant.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.constant.Constant.TAG_APP_INFO_FRAGMENT;

/**
 * Created by norman on 19/05/17.
 */

public class AppInfoActivity extends BaseActivity {

    private static final String LOG_TAG = AppInfoActivity.class.getSimpleName();

//    private RecyclerView mRecyclerView;

    private AppItemDTO mAppItem;

    private FragmentManager mFragmentManager;
    private Fragment mAppInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        //Set initial fragment
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            // The activity is being re-created. Use the
            // savedInstanceState bundle for initializations either
            // during onCreate or onRestoreInstanceState().
            Log.d(LOG_TAG, "onCreate(): activity re-created from savedInstanceState");
            restoreState(savedInstanceState);
        } else {
            // Activity is being created anew.  No prior saved
            // instance state information available in Bundle object.
            Log.d(LOG_TAG, "onCreate(): activity created");

            setupFragments();
        }

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new AppInfoFragment()).commit();
//
//        }

        //Set and populate drawer
        setDrawer(getApplicationContext());
        addDrawerItemFromAppList(null);

    }

    private void restoreState(Bundle savedInstanceState) {
        //Fragments tags were saved in onSavedInstanceState
        mAppInfoFragment = (AppInfoFragment) mFragmentManager.findFragmentByTag(savedInstanceState.getString(TAG_APP_INFO_FRAGMENT));
    }

    private void setupFragments() {
        mAppInfoFragment = new AppInfoFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAppItem = (AppItemDTO) bundle.getParcelable(APP_SELECTED_BUNDLE);
            Log.d(LOG_TAG, "Start new Fragment-->" + mAppItem.getAppName());
            mAppInfoFragment.setArguments(bundle);
        }
        mFragmentManager.beginTransaction().replace(R.id.content_frame_app_info, mAppInfoFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onSaveInstanceState");

        if(null != mAppInfoFragment) {
            savedInstanceState.putString(TAG_APP_INFO_FRAGMENT, mAppInfoFragment.getTag());
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Start new Fragment
     */
    @Override
    public boolean onNavigationItemSelected(DrawerItemDTO item) {
        Fragment fragment = null;
        Bundle bundle = null;

        // Handle navigation view item clicks here.
        int id = item.getId();

        switch (id) {
            case R.id.nav_manage:
                //todo da rivedere
                this.setTitle("Manage");
                fragment = new ManageFragment();
                break;
            default:
                AppItemDTO appItemModel = getAppItemByDrawerId(id);
                bundle = new Bundle();
                bundle.putParcelable(APP_SELECTED_BUNDLE, appItemModel);

                Log.d(LOG_TAG, "Start new Fragment-->" + appItemModel.getAppName());

                fragment = new AppInfoFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_frame_app_info, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(), "inviare feedbak", Toast.LENGTH_SHORT).show();

    }
}
