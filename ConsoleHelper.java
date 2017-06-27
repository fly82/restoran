package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void writeMessage(String message) {
        System.out.print(message);
    }
    public static String readString() throws IOException {
        return reader.readLine();
    }
    public static List<Dish> getAllDishesForOrder() throws IOException {
        String s;
        List<Dish> listOrder = new ArrayList<>();
        writeMessage(Dish.allDishesToString());
        while (!"exit".equals(s=readString())) {
            try {
                listOrder.add(Dish.valueOf(s));
            } catch (IllegalArgumentException e) {
                writeMessage("no such a dish");
            }
        }
        return listOrder;
    }
}
