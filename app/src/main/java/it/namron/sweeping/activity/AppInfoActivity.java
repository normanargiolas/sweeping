package it.namron.sweeping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;

import it.namron.sweeping.adapter.AppItemAdapter;
import it.namron.sweeping.fragment.AppInfoFragment;
import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.fragment.TelegramFragment;
import it.namron.sweeping.fragment.WhatsAppFragment;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.APP_SELECTED_BUNDLE;

/**
 * Created by norman on 19/05/17.
 */

public class AppInfoActivity extends BaseActivity {

    private static final String LOG_TAG = AppInfoActivity.class.getSimpleName();

//    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        set(null, null, this);

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

        //Set initial fragment
        Fragment fragment = new AppInfoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fragment.setArguments(bundle);
            AppItemModel appItem = (AppItemModel) bundle.getParcelable(APP_SELECTED_BUNDLE);
        }


        fragmentManager.beginTransaction().replace(R.id.content_frame_app_info, fragment).commit();


//        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        mRecyclerView.setHasFixedSize(true);
//
//
//        //The AppItemAdapter is responsible for displaying each item in the list.
//        mAppEntryAdapter = new AppItemAdapter(this, mAppListModel, this);
//        mRecyclerView.setAdapter(mAppEntryAdapter);

    }


    //    start new fragment
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
            fragmentManager.beginTransaction().replace(R.id.content_frame_app_info, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
