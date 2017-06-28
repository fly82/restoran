package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.AdvertisementManager;
import com.javarush.task.task27.task2712.ad.NoVideoAvailableException;
import com.javarush.task.task27.task2712.kitchen.Order;

import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

public class Tablet extends Observable {
    final int number;
    private static Logger logger = Logger.getLogger(Tablet.class.getName());

    public Tablet(int number) {
        this.number = number;
    }
    public Order createOrder() {
        Order order = null;
        try {
            order = new Order(this);
            if (order.isEmpty()) return null;
            ConsoleHelper.writeMessage(order.toString());
            setChanged();
            notifyObservers(order);
            AdvertisementManager manager = new AdvertisementManager(10);
            manager.processVideos();
        } catch (NoVideoAvailableException e) {
            logger.log(INFO,"No video is available for the order "+order);
        } catch (IOException e) {
            logger.log(SEVERE, "Console is unavailable.");
        }
        return order;
    }

    @Override
    public String toString() {
        return "Tablet{" +
                "number=" + number +
                '}';
    }
}
