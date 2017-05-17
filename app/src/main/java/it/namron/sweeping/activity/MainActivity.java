package it.namron.sweeping.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.namron.core.utility.AppEntry;
import it.namron.core.utility.AppListLoader;
import it.namron.sweeping.adapter.AppItemAdapter;
import it.namron.sweeping.utils.PackageApp;
import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.fragment.TelegramFragment;
import it.namron.sweeping.fragment.WhatsAppFragment;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.sweeping.R;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<AppItemModel> mAppListModel = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private AppItemAdapter mAppEntryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    /*
     * This number will uniquely identify our Loader
     */
    private static final int ID_APP_LIST_LOADER = 20;
    private LoaderManager mLoaderManager;


    private LoaderManager.LoaderCallbacks<List<AppEntry>> mAppListLoaderCallback = new LoaderManager.LoaderCallbacks<List<AppEntry>>() {

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_APP_LIST_LOADER:
                    // This is called when a new Loader needs to be created.  This
                    // sample only has one Loader with no arguments, so it is simple.
                    return new AppListLoader(getApplicationContext());
                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> appList) {
            if (null == appList) {
                int currentLine = Thread.currentThread().getStackTrace()[0].getLineNumber();
                String mthd = Thread.currentThread().getStackTrace()[0].getMethodName();
                String log = mthd.concat(": ").concat(String.valueOf(currentLine));
                Log.d(LOG_TAG, log);

                Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();
            } else {
                //appList conteins all app installed
                List<AppItemModel> appItemModelList = PackageApp.listOftargetApp(appList);
                mAppEntryAdapter.swapFolder(appItemModelList);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.app_list_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //The AppItemAdapter is responsible for displaying each item in the list.
        mAppEntryAdapter = new AppItemAdapter(getApplicationContext(), mAppListModel);
        mRecyclerView.setAdapter(mAppEntryAdapter);


        //Set initial fragment
//        Fragment fragment = new WhatsAppFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        /*
         * Initialize the loader
         */
        mLoaderManager = getSupportLoaderManager();

        //Insert same data here into bundle
        Bundle pathBundle = new Bundle();
        getSupportLoaderManager().initLoader(ID_APP_LIST_LOADER, pathBundle, mAppListLoaderCallback);


        populateNavigationDrawer(navigationView);


        // show loader and fetch installed app
//        swipeRefreshLayout.post(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        getAppInstalled();
//                    }
//                }
//        );

        Log.d(LOG_TAG, "onCreate done!");
    }

    private void getAppInstalled() {
//        swipeRefreshLayout.setRefreshing(true);
        mAppListModel.clear();

        AppItemModel appListModel;

        appListModel = new AppItemModel();
        appListModel.setAppName("Applicazione1");
        mAppListModel.add(appListModel);

        appListModel = new AppItemModel();
        appListModel.setAppName("Applicazione2");
        mAppListModel.add(appListModel);

        appListModel = new AppItemModel();
        appListModel.setAppName("Applicazione3");
        mAppListModel.add(appListModel);

        mAppEntryAdapter.swapFolder(mAppListModel);
//        swipeRefreshLayout.setRefreshing(false);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void populateNavigationDrawer(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();

        if (appInstalledOrNot(PackageApp.WHATSAPP)) {
            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_whatsapp);
            menu.findItem(R.id.nav_whatsapp).setIcon(icon);
        }

        if (appInstalledOrNot(PackageApp.TELEGRAM)) {
            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_telegram);
            menu.findItem(R.id.nav_telegram).setIcon(icon);
        }

        //Controllare eventuale capacità di memoria
        Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_manage);
        menu.findItem(R.id.nav_manage).setIcon(icon);
    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            Log.d(uri, "Presente!");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(uri, "Non presente!");
        }
        return false;
    }


//    private boolean appInstalled() {
//        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        final List pkgAppsList = getApplicationContext().getPackageManager().queryIntentActivities( mainIntent, 0);
////        pkgAppsList.contains("");
//        return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
}
