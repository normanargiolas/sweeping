package it.namron.sweeping.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import it.namron.sweeping.fragment.ManageFragment;
import it.namron.sweeping.constant.PackageApp;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.fragment.TelegramFragment;
import it.namron.sweeping.fragment.WhatsAppFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOG_TAG = "AppSweeping";
    private static final int PICKFILE_REQUEST_CODE = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Fragment fragment = new ManageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        populateNavigationDrawer(navigationView);

        Log.d(LOG_TAG, "onCreate done!");

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
