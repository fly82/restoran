package com.javarush.task.task27.task2712.ad;

public class AdvertisementStorage {
    private static final AdvertisementStorage instans = new AdvertisementStorage();
    private AdvertisementStorage() {}
    public AdvertisementStorage getInstans() {
        return instans;
    }
}
