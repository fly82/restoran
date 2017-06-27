package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;

public class Restaurant {
    public static void main(String[] args) {

        Cook cook = new Cook("eee");

        Tablet tablet = new Tablet(5);
        tablet.createOrder();
        tablet.addObserver(cook);
    }
}
