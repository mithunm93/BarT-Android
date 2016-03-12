package com.example.mithun.firstapp;

/*
*       !!!NOTE!!!
*       Navigation drawer code was pulled from:
*       http://www.android4devs.com/2014/12/how-to-make-material-design-navigation-drawer.html
*
*       If down, see here:
*       http://webcache.googleusercontent.com/search?q=cache:http://www.android4devs.com/2014/12/how-to-make-material-design-navigation-drawer.html
*
*       modifications were made to fit
*
 */

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class MainActivity extends ActionBarActivity {
    public static MainActivity activity;
    public BluetoothServices mBTService;
    public DrinkQueue drinkQueue;

    private Thread qCheck;
    private Thread bleCheck;

    String NAV_TITLES[] = {"Calibration", "Liquid Remaining", "Admin Lock", "About"};
    RecyclerView mNavRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mNavAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mNavLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView mRecyclerView;
        DrinkAdapter mAdapter;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DrinkAdapter(DrinkManager.getInstance().getDrinks(), R.layout.row_drink, this);
        mRecyclerView.setAdapter(mAdapter);

        mBTService = new BluetoothServices();
        mBTService.setupBT();

        drinkQueue = new DrinkQueue(mBTService);

        // check for drinks in queue periodically
        qCheck = new Thread() {
            public void run() {
                while (true) {
                    // do stuff in a separate thread
                    drinkQueue.sendNextDrink();
                    try {
                        Thread.sleep(3000);    // sleep for 3 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        qCheck.start();

        // check for disconnect periodically
        bleCheck = new Thread() {
            public void run() {
                while (true) {
                    // do stuff in a separate thread
                    //mBTService.keepConnected();
                    mBTService.tryConnect();
                    try {
                        Thread.sleep(10000);    // sleep for 10 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        bleCheck.start();


        // Initializing the Navigation Drawer
        // Code source at top

    /*    mNavRecyclerView = (RecyclerView) findViewById(R.id.NavRecyclerView);
        mNavRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mNavRecyclerView.setLayoutManager(layoutManager1);

        mNavAdapter = new NavAdapter(NAV_TITLES);
        mNavRecyclerView.setAdapter(mNavAdapter);                              // Setting the adapter to RecyclerView
        mNavLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mNavRecyclerView.setLayoutManager(mNavLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mBTService != null)
            menu.findItem(R.id.action_connection)
                    .setIcon(mBTService.ismConnected()
                            ? R.drawable.bluetooth_connected : R.drawable.bluetooth_disabled);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.action_connection)))
            mBTService.setupBT();

        return super.onOptionsItemSelected(item);
    }

}