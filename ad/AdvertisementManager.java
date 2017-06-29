package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.*;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }
    public static long selection(int totalTime, List<Advertisement> items, int numItems, ArrayList<Advertisement> taken) {
        if (numItems == 0 || totalTime == 0)
            return 0;
        if (items.get(numItems-1).getHits()<1 || items.get(numItems-1).getDuration() > totalTime)
            return selection(totalTime, items, numItems - 1, taken);
        else {
            final int preTookSize = taken.size();
            final long took = items.get(numItems-1).getAmountPerOneDisplaying() + selection(totalTime - items.get(numItems - 1).getDuration(), items, numItems - 1, taken);
            final int preLeftSize = taken.size();
            final long left = selection(totalTime, items, numItems - 1, taken);
            if (took > left) {
                if (taken.size() > preLeftSize)
                    taken.subList(preLeftSize, taken.size()).clear();
                taken.add(items.get(numItems - 1));
                return took;
            }
            else {
                if (preLeftSize > preTookSize)
                    taken.subList(preTookSize, preLeftSize).clear();
                return left;
            }
        }
    }
    public void processVideos() {
        List<Advertisement> allAd = storage.list();
        int s = allAd.size();
        ArrayList<Advertisement> result = new ArrayList<>();
        int totalTime = timeSeconds;
        long totalAmount = selection(totalTime,allAd,s,result);
        if (result.isEmpty()) {
            throw new NoVideoAvailableException();
        }

        if(result.isEmpty())throw new NoVideoAvailableException();

        Collections.sort(result, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                long a = o1.getAmountPerOneDisplaying();
                long b = o2.getAmountPerOneDisplaying();
                if (a!=b) return Long.compare(a, b);
                return Long.compare(a * 1000 / o1.getDuration(), b * 1000 / o2.getDuration());
            }
        });

        for (Advertisement v:result) {
            long a = v.getAmountPerOneDisplaying();
            ConsoleHelper.writeMessage(String.format("%s Video is displaying... %s, %s", v.getName(), a, a*1000/v.getDuration()));
            v.revalidate();
        }
    }
}
