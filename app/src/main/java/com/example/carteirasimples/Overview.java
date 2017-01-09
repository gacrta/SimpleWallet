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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Overview extends AppCompatActivity implements AddValueFragment.AddValueListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final Uri CONTENT_URI = WalletValuesContract.WalletItens.CONTENT_URI;
    private static final String SELECTED_ITEM = "item";
    private static final int WALLET_VIEW_ID = 0;
    private static final int CHART_VIEW_ID = 1;
    private static final int INFO_VIEW_ID = 2;
    private static final int ID_FULL_TABLE = 0;
    private static final int ID_INCOMES = 1;
    private static final int ID_OUTCOMES = 2;
    private static final String[] income = {"true"};
    private static final String[] outcome = {"false"};

    List<WalletValue> valuesAdded;
    private Handler mHandler;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abt;
    private String[] fragmentsTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        valuesAdded = new ArrayList<>();
        fragmentsTitles = getResources().getStringArray(R.array.fragments_titles);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        getSupportLoaderManager().initLoader(ID_INCOMES, null, this);
        getSupportLoaderManager().initLoader(ID_OUTCOMES, null, this);

        
        if(drawerLayout != null) {
            // we are at portrait mode

            abt = new ActionBarDrawerToggle(
                    this, drawerLayout,
                    R.string.menu_drawer_open, R.string.menu_drawer_close){

                @Override
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    fab.show();
                    changeActionBarTitle();
                    invalidateOptionsMenu();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    fab.hide();
                    updateSummary();
                    getSupportActionBar().setTitle(R.string.main_activity_title);
                    invalidateOptionsMenu();
                }
            };

            drawerLayout.addDrawerListener(abt);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {
            updateSummary();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (drawerLayout != null)
                    drawerLayout.closeDrawers();

                int idView = R.id.fragment_container;
                switch (item.getItemId()) {
                    case R.id.drawer_view_list:
                        WalletListFragment wlf = new WalletListFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(idView, wlf, getString(R.string.fragment_wallet_list_tag)).commit();
                        getSupportActionBar().setTitle(fragmentsTitles[WALLET_VIEW_ID]);
                        //initWalletListDB();
                        break;
                    case R.id.drawer_show_chart:
                        ChartsFragment cf = new ChartsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(idView, cf, getString(R.string.fragment_chart_tag)).commit();
                        getSupportActionBar().setTitle(fragmentsTitles[CHART_VIEW_ID]);
                        break;
                    case R.id.drawer_info:
                        AppInfoFragment aif = new AppInfoFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(idView, aif, getString(R.string.fragment_app_info_tag)).commit();
                        getSupportActionBar().setTitle(fragmentsTitles[INFO_VIEW_ID]);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            GreetingsFragment greetings = new GreetingsFragment();
            fm.beginTransaction().replace(R.id.fragment_container, greetings,
                    getString(R.string.fragment_greetings_tag)).commit();
        }

        else {
            int selectedItem = savedInstanceState.getInt(SELECTED_ITEM);
            switch (selectedItem) {
                case WALLET_VIEW_ID:
                    WalletListFragment wlf = new WalletListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, wlf, getString(R.string.fragment_wallet_list_tag)).commit();
                    getSupportActionBar().setTitle(fragmentsTitles[WALLET_VIEW_ID]);
                    //initWalletListDB();
                    break;
                case CHART_VIEW_ID:
                    ChartsFragment cf = new ChartsFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, cf, getString(R.string.fragment_chart_tag)).commit();
                    getSupportActionBar().setTitle(fragmentsTitles[CHART_VIEW_ID]);
                    break;
                case INFO_VIEW_ID:
                    AppInfoFragment aif = new AppInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, aif, getString(R.string.fragment_app_info_tag)).commit();
                    getSupportActionBar().setTitle(fragmentsTitles[INFO_VIEW_ID]);
                    break;
            }
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what){
                    case 1:
                        showMessage();
                        updateSummary();
                        break;
                    default:
                        super.handleMessage(inputMessage);
                }
            }
        };

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

        if (findViewById(R.id.drawer_layout) != null) {
            //noinspection SimplifiableIfStatement
            if (abt.onOptionsItemSelected(item)) {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(findViewById(R.id.drawer_layout) != null) {
            abt.syncState();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abt.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int fragId = 0;
        if (getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_wallet_list_tag)) != null) {
            fragId = WALLET_VIEW_ID;
        }
        else if (getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_chart_tag)) != null) {
            fragId = CHART_VIEW_ID;
        }
        else if (getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_app_info_tag)) != null) {
            fragId = INFO_VIEW_ID;
        }

        outState.putInt(SELECTED_ITEM, fragId);
        super.onSaveInstanceState(outState);
    }

    private void changeActionBarTitle() {
        if (getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_wallet_list_tag)) != null) {
            getSupportActionBar().setTitle(fragmentsTitles[WALLET_VIEW_ID]);
        }
        else if (getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_chart_tag)) != null) {
            getSupportActionBar().setTitle(fragmentsTitles[CHART_VIEW_ID]);
        }
        else if (getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_app_info_tag)) != null) {
            getSupportActionBar().setTitle(fragmentsTitles[INFO_VIEW_ID]);
        }
    }

    // function that calls snackbar after a adding a new value
    private void showMessage() {
        final Context context = this;
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),
                getString(R.string.added), Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.button_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sorry, does nothing yet!", Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    // function that adds last WalletValue to Content Provider and calls showMessage
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

    // function that shows fab after addValueFragment ends
    public void dismissAddValueFragment() {
        fab.show();
    }

    // function to initWalletListDB
    public void initWalletListDB(){
        getSupportLoaderManager().initLoader(ID_FULL_TABLE, null, this);
    }

    /* - LoaderManager.LoaderCallbacks functions - */


    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] value = {WalletValuesContract.WalletItens.COLUMN_VALUE};
        String selection = WalletValuesContract.WalletItens.COLUMN_SIGN + " = ?";
        switch (id) {
            case ID_FULL_TABLE:
                return new CursorLoader(this,
                        CONTENT_URI, WalletValuesContract.WalletItens.PROJECTION_ALL,
                        null, null,
                        WalletValuesContract.WalletItens.SORT_ORDER_DEFAULT);
            case ID_INCOMES:

                return new CursorLoader(this,
                        CONTENT_URI, value,
                        selection, income, null);
            case ID_OUTCOMES:
                return new CursorLoader(this,
                        CONTENT_URI, value,
                        selection, outcome, null);
            default:
                return null;
        }
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        float sum;
        int id = cursorLoader.getId();
        WalletListFragment listFrag;
        switch (id) {
            case ID_FULL_TABLE:
                listFrag = (WalletListFragment) getSupportFragmentManager()
                    .findFragmentByTag(getString(R.string.fragment_wallet_list_tag));
                if(listFrag.getAdapter() == null) {
                    listFrag.setAdapter();
                }
                listFrag.getAdapter().swapCursor(cursor);
                break;
            case ID_INCOMES:
                sum = getSum(cursor);
                TextView tv_income = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_income_value);
                tv_income.setText(String.format(java.util.Locale.getDefault(),"%.2f", sum));
                updateSummary();
                break;
            case ID_OUTCOMES:
                sum = getSum(cursor);
                TextView tv_outcome = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_outcome_value);
                tv_outcome.setText(String.format(java.util.Locale.getDefault(),"%.2f", sum));
                updateSummary();
                break;
        }
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        switch (cursorLoader.getId()){
            case ID_FULL_TABLE:
                if ((getSupportFragmentManager()
                        .findFragmentByTag(getString(R.string.fragment_wallet_list_tag))) != null)
                ((WalletListFragment)getSupportFragmentManager()
                        .findFragmentByTag(getString(R.string.fragment_wallet_list_tag)))
                        .getAdapter().swapCursor(null);
        }
    }

    // function that populates savedValues array based on content provider
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

    public float getSum(Cursor cursor) {
        float sum = 0;
        float currValue;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            currValue = Float.parseFloat(cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_VALUE)));
            sum += currValue;
            cursor.moveToNext();
        }
        return sum;
    }

    // function that evaluates balance and put values on textViews
    protected void updateSummary() {
        String incString = ((TextView) navigationView.getHeaderView(0)
                .findViewById(R.id.tv_income_value)).getText().toString();
        String outString = ((TextView) navigationView.getHeaderView(0)
                .findViewById(R.id.tv_outcome_value)).getText().toString();

        NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.getDefault());
        java.text.DecimalFormat df = (java.text.DecimalFormat) nf;
        df.applyPattern("##.00");

        if (!incString.isEmpty() && !outString.isEmpty()) {
            float inc;
            float out;

            try {
                inc = df.parse(incString).floatValue();
                out = df.parse(outString).floatValue();
            } catch (ParseException e) {
                Log.v("UPDATE_SUMMARY", "Cannot parse value");
                return;
            }
            float balance = inc - out;

            TextView tv_balance = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_balance_value);
            tv_balance.setText(String.format(java.util.Locale.getDefault(),"%.2f", balance));
            if (balance < 0.0) {
                tv_balance.setTextColor(Color.parseColor("#a00103"));
            }
            else {
                tv_balance.setTextColor(Color.parseColor("#74ba48"));
            }
        }
    }
}