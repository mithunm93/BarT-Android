package com.example.mithun.firstapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mithun on 1/29/15.
 */
public class DrinkDetailActivity extends Activity
{

    public static DrinkDetailActivity activity;
    public Drink drink;

    private static final int TRANS_DUR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_drink);

        activity = this;

        //grab Drink parcel
        drink = getIntent().getParcelableExtra("drink");

        //set up image
        ImageView i = (ImageView) findViewById(R.id.drinkImage);
        i.setImageDrawable(this.getResources().getDrawable(drink.getImageResourceID(this)));

        //set up text
        TextView t = (TextView) findViewById(R.id.drinkName);
        t.setText(drink.name);

        t = (TextView) findViewById(R.id.drinkDesc);
        t.setText(drink.desc);

        //set up FAB onClickListener
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //create and display the dialog
                DialogFragment orderFragment = new OrderDrinkDialogFragment();
                orderFragment.show(getFragmentManager(), getString(R.string.order_dialog_tag));
            }
        });

        Transition ts = new Slide();

        //don't want status bar and navigation bar to slide across screen
        ts.excludeTarget(android.R.id.statusBarBackground, true);
        ts.excludeTarget(android.R.id.navigationBarBackground, true);
        ts.setDuration(TRANS_DUR);

        getWindow().setEnterTransition(ts);
        getWindow().setExitTransition(ts);

        //This line gets it to work! As Pavel suggested
        getWindow().setSharedElementsUseOverlay(false);
    }
}
