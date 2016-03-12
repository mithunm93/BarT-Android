package com.example.mithun.firstapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Mithun on 4/6/15.
 */
public class DrinkQueue
{
    private LinkedList<DrinkOrder> orderList;
    private int numOrders;
    private boolean isFree; // BarT isFree
    private BluetoothServices bServices;


    public DrinkQueue(BluetoothServices bs)
    {
        bServices = bs;
        numOrders = 0;
        orderList = new LinkedList<>();
        isFree = false;
    }


    public int addDrinkOrder(DrinkOrder order)
    {
        orderList.add(order);

        return ++numOrders;
    }

    public int getNumDrinks()
    {
        return numOrders;
    }

    public void setStatus(char free)
    {
        isFree = (free == 'F');
    }

    public void sendNextDrink()
    {

        // BarT needs to be free and there needs to be an order in the queue to send
        if (!isFree || orderList.peekLast() == null) return;

        DrinkOrder order = orderList.removeLast();

        // Format: { 'Z', name length, name, drinkNum}
        //  'Z' is an indication that this is a drink order
        // Example: { 'Z', '6', "Mithun", '8'}

        char[] data = new char[1 + 1 + order.userName.length() + 1];
        int i = 0;

        // Z is temporary, to select the "custom data" mode on Arduino
        data[i++] = 'Z';

        // name
        data[i++] = Character.toChars(order.userName.length())[0];
        for (int j = 0; j < order.userName.length(); j++)
            data[i++] = order.userName.charAt(j);

        // this should be drinkNum
        data[i++] = Character.toChars(order.drink.drinkNum)[0];

        numOrders--;

        bServices.sendData(data);
    }

}
