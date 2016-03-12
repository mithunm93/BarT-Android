package com.example.mithun.firstapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mithun on 1/26/15.
 */
public class Drink implements Parcelable
{
    public String name;
    public String desc;
    public String imageName;
    public int drinkNum;

    public Drink(String n, String d, String iN, int num)
    {
        name = n;
        desc = d;
        imageName = iN;
        drinkNum = num;
    }

    public int getImageResourceID(Context context)
    {
        //this ideally shouldn't exist
        String imageName = "b";

        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

        /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(desc);
        out.writeString(imageName);
        out.writeInt(drinkNum);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Drink> CREATOR = new Parcelable.Creator<Drink>() {
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    private Drink(Parcel in)
    {
        name = in.readString();
        desc = in.readString();
        imageName = in.readString();
        drinkNum = in.readInt();
    }

}
