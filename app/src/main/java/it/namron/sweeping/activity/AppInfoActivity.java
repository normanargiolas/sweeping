package it.namron.sweeping.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import it.namron.sweeping.fragment.AppInfoFragment;
import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.model.DrawerItemModel;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.APP_SELECTED_BUNDLE;

/**
 * Created by norman on 19/05/17.
 */

public class AppInfoActivity extends BaseActivity {

    private static final String LOG_TAG = AppInfoActivity.class.getSimpleName();

//    private RecyclerView mRecyclerView;

    AppItemModel mAppItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);


        if (savedInstanceState != null) {
            // The activity is being re-created. Use the
            // savedInstanceState bundle for initializations either
            // during onCreate or onRestoreInstanceState().
            Log.d(LOG_TAG,
                    "onCreate(): activity re-created from savedInstanceState");

        } else {
            // Activity is being created anew.  No prior saved
            // instance state information available in Bundle object.
            Log.d(LOG_TAG,
                    "onCreate(): activity created");
        }

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new AppInfoFragment()).commit();
//
//        }

        //Set and populate drawer
        setDrawer(getApplicationContext());
        addDrawerItemFromAppList(null);

        //Set initial fragment
        Fragment fragment = new AppInfoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAppItem = (AppItemModel) bundle.getParcelable(APP_SELECTED_BUNDLE);
            Log.d(LOG_TAG, "Start new Fragment-->" + mAppItem.getAppName());
            fragment.setArguments(bundle);
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame_app_info, fragment).commit();

    }


    /**
     * Start new Fragment
     */
    @Override
    public boolean onNavigationItemSelected(DrawerItemModel item) {
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
                AppItemModel appItemModel = getAppItemByDrawerId(id);
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
}
