package it.namron.sweeping.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.adapter.AppItemAdapter;
import it.namron.sweeping.concurrency.AppEntry;
import it.namron.sweeping.concurrency.AppListLoader;
import it.namron.sweeping.dto.AppItemDTO;
import it.namron.sweeping.dto.DrawerItemDTO;
import it.namron.sweeping.fragment.AppInfoFragment;
import it.namron.sweeping.fragment.HistoryFragment;
import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.AppUtils;

import static it.namron.sweeping.constant.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.constant.Constant.ID_APP_LIST_LOADER;
import static it.namron.sweeping.constant.Constant.ON_HISTORY_PARAM;
import static it.namron.sweeping.constant.Constant.TAG_APP_INFO_FRAGMENT;
import static it.namron.sweeping.utils.LogUtils.LOGD;


public class MainActivity extends BaseActivity implements
        AppItemAdapter.AppItemAdapterOnClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<AppItemDTO> mAppListModel = new ArrayList<>();
    private List<DrawerItemDTO> mDrawerListModel = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private AppItemAdapter mAppEntryAdapter;

    private LinearLayout mContentLayout;

//    private SwipeRefreshLayout swipeRefreshLayout;

    private Toast mToast;

    private boolean onHistory = false;

    /**
     * A value that uniquely identifies the request to download an
     * image.
     */
    private static final int APP_INFO_REQUEST = 1;


    private LoaderManager mLoaderManager;


    private LoaderManager.LoaderCallbacks<List<AppEntry>> mAppListLoader = new LoaderManager.LoaderCallbacks<List<AppEntry>>() {

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
                List<AppItemDTO> appItemModelList = AppUtils.listOfTargetApp(appList);

                List<AppItemDTO> appDirItemModelList = AppUtils.listOfTargetDir(appList);
                if (appDirItemModelList != null) {
                    appItemModelList.addAll(appDirItemModelList);
                }

                if (appItemModelList != null) {
                    mAppListModel = appItemModelList;
                    addDrawerItemFromAppList(mAppListModel);
                    mAppEntryAdapter.swapFolder(mAppListModel);
                } else {
                    //todo vedere meglio se appItemModelList==null
                    //nessuna applicazione supportata

                }
                Log.d(LOG_TAG, "onLoadFinished: appItemModelList=null");

            }
        }


        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LOGD(LOG_TAG, "onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ErrorLogDAO errorLogDAO = new ErrorLogDAO();
//        //Insert Sample data
//        ErrorLog errorLog = new ErrorLog();
//        errorLog.setFile("nome del file");
//        errorLog.setMethod("descrizione metodo");
//        errorLog.setLine("numero linea");
//        errorLog.setMsg("messaggio");
//        errorLog.setLog("log");
//        errorLog.setStackTrace("stacktrace");
//        errorLogDAO.insert(errorLog);
//
//        errorLogDAO.insert(errorLog);
//        errorLogDAO.insert(errorLog);


        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
//        ResourceHashCode.setWritableDatabase(dbHelper.getWritableDatabase());


//        setLayout(R.layout.activity_main);
        setDrawer(getApplicationContext());

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mContentLayout = (LinearLayout) findViewById(R.id.content_frame);

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
        mLoaderManager.initLoader(ID_APP_LIST_LOADER, pathBundle, mAppListLoader);


        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "onCreate(): activity re-created from savedInstanceState");
            restoreState(savedInstanceState);
        }

//        populateNavigationDrawer(navigationView);


//        //Set singleton resource hash code
//        ResourceHashCode.getInstance(getApplicationContext());
        Log.d(LOG_TAG, "onCreate done!");
    }

    private void restoreState(Bundle savedInstanceState) {
        onHistory = savedInstanceState.getBoolean(ON_HISTORY_PARAM);
        if (onHistory) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }
            setOnHistoryFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        if (onHistory) {
            savedInstanceState.putBoolean(ON_HISTORY_PARAM, onHistory);
        }
        super.onSaveInstanceState(savedInstanceState);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


//    private void populateNavigationDrawer(NavigationView navigationView) {
//        Menu menu = navigationView.getMenu();
//
//        if (appInstalledOrNot(AppUtils.WHATSAPP)) {
//            Drawable icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_whatsapp);
//            menu.findItem(R.id.nav_whatsapp).setIcon(icon);
//        }
//
//        if (appInstalledOrNot(AppUtils.TELEGRAM)) {
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

    /**
     * This method is used to notify that a list item has clicked in the object that implement
     * AppItemAdapterOnClickListener in AppItemAdapter.
     */
    @Override
    public void onListItemClick(AppItemDTO clickedItem) {

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

    private void startAppInfoActivity(AppItemDTO clickedItem) {
        try {
            Intent appInfoIntent = makeAppInfoIntent(clickedItem);
            if (appInfoIntent != null)
                startActivityForResult(appInfoIntent, APP_INFO_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent makeAppInfoIntent(AppItemDTO clickedItem) {
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
    public boolean onNavigationItemSelected(DrawerItemDTO item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getId();


        switch (id) {
            case R.id.nav_manage:
                //todo start activity
                this.setTitle("Manage");
                fragment = new ManageFragment();
                break;
            default:
                if (mAppListModel != null) {
                    AppItemDTO appItemModel = mAppListModel.get(id);
                    Log.d(LOG_TAG, "Start new Activity-->" + appItemModel.getAppName());

                    startAppInfoActivity(appItemModel);
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            if (mRecyclerView.getParent() == null) {

                mContentLayout.addView(mRecyclerView);
            }

            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    /**
     * Start new Fragment with a list of log selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_history:
                setOnHistoryFragment();
                return true;
            case R.id.action_error:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setOnHistoryFragment() {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new HistoryFragment();
        if (mRecyclerView.getParent() != null) {
            ((LinearLayout) mRecyclerView.getParent()).removeView(mRecyclerView);
        }
        onHistory = true;
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }
}

