package com.example.mithun.firstapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Mithun on 2/11/15.
 */
public class OrderDrinkDialogFragment extends DialogFragment
{
    public boolean doubleShot = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(DrinkDetailActivity.activity, R.style.DialogTheme);

        //set dialog title
        builder.setTitle(R.string.dialog_title);

        builder.setView(R.layout.order_drink_dialog);

        //MultiChoice for double shot and mixed with soda
        builder.setMultiChoiceItems(R.array.dialog_checkboxes,null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        switch (which)
                        {
                            case 0:
                                doubleShot = isChecked;
                                break;
                        }
                    }
                });


        builder
                .setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                       MainActivity.activity.drinkQueue.addDrinkOrder(new DrinkOrder(getName(), DrinkDetailActivity.activity.drink));
                       // TODO check before toast
                       Toast orderedToast =
                               Toast.makeText(DrinkDetailActivity.activity, R.string.dialog_ordered_toast, Toast.LENGTH_SHORT);
                       orderedToast.show();
                    }
                })
                .setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

        Dialog dialog = builder.create();

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }

    private String getName()
    {
        return ((EditText) getDialog().findViewById(R.id.name)).getText().toString();
    }

}
