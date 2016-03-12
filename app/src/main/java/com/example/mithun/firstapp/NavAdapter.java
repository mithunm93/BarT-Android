package com.example.mithun.firstapp;

/*
*       !!!NOTE!!!
*       All code was pulled from:
*       http://www.android4devs.com/2014/12/how-to-make-material-design-navigation-drawer.html
*
*       If down, see here:
*       http://webcache.googleusercontent.com/search?q=cache:http://www.android4devs.com/2014/12/how-to-make-material-design-navigation-drawer.html
*
*       modifications were made to fit
*
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mithun on 4/21/15.
 */
public class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder>
{

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.rowText);
        }
    }

    NavAdapter(String Titles[])
    {
        mNavTitles = Titles;
    }


    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public NavAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_item_layout,parent,false); //Inflating the layout

            return new ViewHolder(v); //Creating ViewHolder and passing the object of type view

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i)
    {
        int temp = 1;      // this line put in because of Android studio bug

        if (i < mNavTitles.length)
            holder.textView.setText(mNavTitles[i]); // Setting the Text with the array of our Titles
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount()
    {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }

}

