package com.example.mithun.firstapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mithun on 1/26/15.
 */
public class DrinkManager
{
    private static String[] drinkArray = {"Orange Juice",
                                            "Apple Juice",
                                            "Cranberry Juice",
                                            "Water",
                                            "Apple Cranberry Juice",
                                            "Orange Cranberry Water",
                                            "Apple Cranberry Orange Water Juice"};
    private static String[] drinkDescArray = {"Just regular old OJ, the reason I never brush my teeth before breakfast",
                                                "Apple Juice, almost as American as its cousin, the pie",
                                                "Cranberries take all of nature's worst ingredients and put into one little fruit",
                                                "H2O, drink 8 cups",
                                                "Apples and Cranberries play nicely together, you almost can't taste the cranberry. Almost.",
                                                "This is not the drink you are looking for",
                                                "An amalgamation of all our ingredients, the term 'Juice' is used very loosely. Bet you wish we could have alcohol now, don't you?"};
    private static DrinkManager mInstance;
    private List<Drink> drinks;

    public static DrinkManager getInstance()
    {
        if (mInstance == null)
            mInstance = new DrinkManager();

        return mInstance;
    }

    public List<Drink> getDrinks()
    {
        if (drinks == null)
        {
            drinks = new ArrayList<Drink>();

            for (int i = 0; i < drinkArray.length; i++)
            {
                Drink drink = new Drink(drinkArray[i],
                                        drinkDescArray[i],
                                        drinkArray[i].replaceAll("\\s+","").toLowerCase(),
                                        i);
                drinks.add(drink);
            }
        }

        return drinks;
    }



}
