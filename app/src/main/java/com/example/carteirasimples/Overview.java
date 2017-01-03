package com.example.carteirasimples;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Overview extends AppCompatActivity implements AddValueFragment.AddValueListener,
        WalletOverviewFragment.ViewWalletListListener {

    List<WalletValue> valuesAdded;
    Handler mHandler;
    private static final Uri CONTENT_URI = WalletValuesContract.WalletItens.CONTENT_URI;
    FloatingActionButton fab;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        valuesAdded = new ArrayList<>();
        readWalletValuesDatabase();

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    default:
                        return true;
                }
            }
        });

        abt = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.menu_drawer_open, R.string.menu_drawer_close){

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                fab.show();
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                fab.hide();
                invalidateOptionsMenu();
            }
        };

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout.addDrawerListener(abt);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what){
                    case 1:
                        showMessage();
                        WalletOverviewFragment fragment1 = (WalletOverviewFragment) getSupportFragmentManager()
                                .findFragmentByTag(getString(R.string.fragment_overview_tag));
                        if(fragment1 != null) {
                            fragment1.updateSummary();
                        }
                        WalletListFragment fragment2 = (WalletListFragment) getSupportFragmentManager()
                                .findFragmentByTag(getString(R.string.fragment_wallet_list_tag));
                        if(fragment2 != null) {
                            fragment2.updateList();
                        }
                        break;
                    default:
                        super.handleMessage(inputMessage);
                }
            }
        };

        FragmentManager fm = getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WalletOverviewFragment overview = new WalletOverviewFragment();
            WalletListFragment walletList = new WalletListFragment();
            fm.beginTransaction().replace(R.id.fragment_overview, overview,
                    getString(R.string.fragment_overview_tag)).commit();
            fm.beginTransaction().replace(R.id.fragment_wallet_list, walletList,
                    getString(R.string.fragment_wallet_list_tag)).commit();
        }
        else {
            WalletOverviewFragment overview = new WalletOverviewFragment();
            fm.beginTransaction().replace(R.id.fragment_container, overview,
                    getString(R.string.fragment_overview_tag)).commit();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addDialog = new AddValueFragment();
                fab.hide();
                addDialog.show(getSupportFragmentManager(), getString(R.string.add_value_fragment_tag));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (abt.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        //boolean drawerOpen = drawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        abt.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abt.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager fm = getSupportFragmentManager();
        fm.executePendingTransactions();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            WalletOverviewFragment overview = (WalletOverviewFragment) fm.findFragmentByTag(
                    getString(R.string.fragment_overview_tag));
            fm.beginTransaction().remove(overview).commit();
            WalletListFragment walletList = (WalletListFragment) fm.findFragmentByTag(
                    getString(R.string.fragment_wallet_list_tag));
            fm.beginTransaction().remove(walletList).commit();
        }
        else {
            Fragment overviewContainer = fm.findFragmentById(R.id.fragment_overview);
            Fragment walletListContainer = fm.findFragmentById(R.id.fragment_wallet_list);
            if (walletListContainer != null) {
                fm.popBackStack();
                fm.beginTransaction().remove(walletListContainer).commit();
            }
            if (overviewContainer != null) {
                fm.beginTransaction().remove(overviewContainer).commit();
            }
        }
        fm.executePendingTransactions();
        super.onSaveInstanceState(outState);
    }

    private void showMessage() {
        final Context context = this;
        Snackbar snackbar;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            snackbar = Snackbar.make(findViewById(R.id.drawer_layout),
                    getString(R.string.added), Snackbar.LENGTH_LONG);
        }
        else {
            snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_land),
                    getString(R.string.added), Snackbar.LENGTH_LONG);
        }
        snackbar.setAction(R.string.button_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sorry, does nothing yet!", Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        WalletValue newValue = valuesAdded.get(valuesAdded.size()-1);

        ContentValues contentValues = new ContentValues();
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_VALUE, Float.toString(newValue.getValue()));
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_CATEGORY, newValue.getCategory());
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_DATE, newValue.getDate());
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_SIGN, Boolean.toString(newValue.getSign()));
        getContentResolver().insert(CONTENT_URI, contentValues);

        Message message = mHandler.obtainMessage(1,newValue);
        mHandler.sendMessage(message);
    }

    public void dismissAddValueFragment() {
        fab.show();
    }

    public void readWalletValuesDatabase() {
        Cursor cursor = getContentResolver().query(CONTENT_URI, WalletValuesContract.WalletItens.PROJECTION_ALL, null, null,
                WalletValuesContract.WalletItens.SORT_ORDER_DEFAULT);
        try {
            cursor.moveToFirst();
            WalletValue newValue;
            while (!cursor.isAfterLast()) {
                String strValue = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_VALUE));
                String strCategory = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_CATEGORY));
                String strDate = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_DATE));
                String strSign = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_SIGN));
                newValue = new WalletValue(Float.parseFloat(strValue), strCategory, strDate, Boolean.parseBoolean(strSign));
                valuesAdded.add(newValue);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (NullPointerException e) {
            throw new NullPointerException("Not possible to read database");
        }
    }

    public void viewWalletDetails() {
        WalletListFragment secondFragment = new WalletListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, secondFragment, getString(R.string.fragment_wallet_list_tag));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}