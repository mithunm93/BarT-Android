package com.example.mithun.firstapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.graphics.Palette;

/**
 * Created by Mithun on 1/26/15.
 */
public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder>
{

    private List<Drink> drinks;
    private int rowLayout;
    private Context mContext;

    public DrinkAdapter(List<Drink> drinks, int rowLayout, Context context) {
        this.drinks = drinks;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Drink drink = drinks.get(i);

        viewHolder.setDrink(mContext, drink);
    }

    @Override
    public int getItemCount() {
        return drinks == null ? 0 : drinks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public Drink drink;

        public TextView drinkName;
        //public TextView drinkDesc;
        public ImageView drinkImage;

       //will set the views in adapter based on Drink object
        public void setDrink(Context context, Drink d)
        {
            drink = d;

            drinkName.setText(drink.name);
            drinkImage.setImageDrawable(context.getResources().getDrawable(drink.getImageResourceID(context)));
            //drinkDesc.setText(drink.desc);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            drinkName = (TextView) itemView.findViewById(R.id.drinkName);
            //drinkDesc = (TextView) itemView.findViewById(R.id.drinkDesc);
            drinkImage = (ImageView)itemView.findViewById(R.id.drinkImage);

            itemView.setOnClickListener (new View.OnClickListener() {

                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(MainActivity.activity, DrinkDetailActivity.class);

                    //so the new activity knows which image to access
                    //a better way would be to access the drink object and pass it
                    intent.putExtra("drink", drink);

                    //add all the views we want to transition along to pairs
                    List<Pair<View, String>> pairs = new ArrayList<>();

                    //## IMPROPER ##
                    // we want the navigation bar to transition with us, otherwise the images clip over it on transition
                    pairs.add(Pair.create(MainActivity.activity.findViewById(android.R.id.navigationBarBackground),
                        Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));

                    pairs.add(Pair.create(MainActivity.activity.findViewById(android.R.id.statusBarBackground),
                        Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));

                    //the image transitions, of course
                    pairs.add(Pair.create((View)drinkImage, MainActivity.activity.getString(R.string.drink_image_transition)));

                    //create the options
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.activity,
                            pairs.toArray(new Pair[pairs.size()]));

                    //start new activity
                    MainActivity.activity.startActivity(intent, options.toBundle());


                }
            });

        }

    }


}
