package it.namron.sweeping.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.namron.core.utility.AppEntry;
import it.namron.core.utility.AppListLoader;
import it.namron.sweeping.adapter.AppItemAdapter;
import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.PackageApp;

import static it.namron.sweeping.utils.Constant.APP_SELECTED_BUNDLE;


public class MainActivity extends BaseActivity implements AppItemAdapter.AppItemAdapterOnClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<AppItemModel> mAppListModel = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private AppItemAdapter mAppEntryAdapter;
//    private SwipeRefreshLayout swipeRefreshLayout;

    private Toast mToast;


    /**
     * A value that uniquely identifies the request to download an
     * image.
     */
    private static final int APP_INFO_REQUEST = 1;

    /**
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
                List<AppItemModel> appItemModelList = PackageApp.listOfTargetApp(appList);
                
                List<AppItemModel> appDirItemModelList = PackageApp.listOfTargetDir(appList);
                if (appDirItemModelList != null)
                    appItemModelList.addAll(appDirItemModelList);

                mAppListModel = appItemModelList;
                addDrawerItem(mAppListModel);
                mAppEntryAdapter.swapFolder(mAppListModel);
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

        setDrawer(getApplicationContext());


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.app_list_recycler);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);


        //The AppItemAdapter is responsible for displaying each item in the list.
        mAppEntryAdapter = new AppItemAdapter(this, mAppListModel, this);
        mRecyclerView.setAdapter(mAppEntryAdapter);

        /*
         * Initialize the loader
         */
        mLoaderManager = getSupportLoaderManager();

        //Insert same data here into bundle
        Bundle pathBundle = new Bundle();
        getSupportLoaderManager().initLoader(ID_APP_LIST_LOADER, pathBundle, mAppListLoaderCallback);


//        populateNavigationDrawer(navigationView);

        Log.d(LOG_TAG, "onCreate done!");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


//    private void populateNavigationDrawer(NavigationView navigationView) {
//        Menu menu = navigationView.getMenu();
//
//        if (appInstalledOrNot(PackageApp.WHATSAPP)) {
//            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_whatsapp);
//            menu.findItem(R.id.nav_whatsapp).setIcon(icon);
//        }
//
//        if (appInstalledOrNot(PackageApp.TELEGRAM)) {
//            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_telegram);
//            menu.findItem(R.id.nav_telegram).setIcon(icon);
//        }
//
//        //Controllare eventuale capacità di memoria
//        Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_manage);
//        menu.findItem(R.id.nav_manage).setIcon(icon);
//    }


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


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

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


    @Override
    public void onListItemClick(AppItemModel clickedItem) {

        /*
         * Even if a Toast isn't showing, it's okay to cancel it. Doing so
         * ensures that our new Toast will show immediately, rather than
         * being delayed while other pending Toasts are shown.
         *
         * Comment out these three lines, run the app, and click on a bunch of
         * different items if you're not sure what I'm talking about.
         */
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        String toastMessage = clickedItem.getAppName() + " clicked.";
//        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
//
//        mToast.show();
        Log.d(LOG_TAG, "Start new Activity-->" + clickedItem.getAppName());

        startAppInfoActivity(clickedItem);
    }

    private void startAppInfoActivity(AppItemModel clickedItem) {
        try {
            Intent appInfoIntent = makeAppInfoIntent(clickedItem);
            if (appInfoIntent != null)
                startActivityForResult(appInfoIntent, APP_INFO_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent makeAppInfoIntent(AppItemModel clickedItem) {
        Class destinationActivity = AppInfoActivity.class;
        Bundle bundle = new Bundle();
        bundle.putParcelable(APP_SELECTED_BUNDLE, clickedItem);

        Intent intent = new Intent(getApplicationContext(), destinationActivity);
        intent.putExtras(bundle);

        return intent;
    }

    /**
     * Start new Activity
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {
            case R.id.nav_manage:
                //todo start activity
                this.setTitle("Manage");
                fragment = new ManageFragment();
                break;
            default:
                if (mAppListModel != null) {
                    AppItemModel appItemModel = mAppListModel.get(id);
                    Log.d(LOG_TAG, "Start new Activity-->" + appItemModel.getAppName());

                    startAppInfoActivity(appItemModel);
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

