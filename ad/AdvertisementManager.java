package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.*;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    private class VideoSet{
        private int fullDuration;
        private long fullAmount;
        private ArrayList<Advertisement>videoSet;
        public VideoSet(ArrayList<Advertisement> videoSet) {
            this.videoSet = videoSet;
            for (Advertisement advertisement : videoSet) {
                fullDuration +=advertisement.getDuration();
                fullAmount += advertisement.getAmountPerOneDisplaying();
            }
        }
    }

    private List<Advertisement> searchVideo(List<Advertisement> list, List<VideoSet> sets, int mask, int limit) {
        ArrayList<Advertisement> set;
        if (mask < limit) {
            set = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if ((mask & (1 << i)) != 0) {
                    set.add(list.get(i));
                }
            }
            VideoSet videoSet = new VideoSet(set);
            if (videoSet.fullDuration <= timeSeconds) {
                sets.add(videoSet);
            }
            searchVideo(list, sets, mask + 1, limit);
        }
        Collections.sort(sets, new Comparator<VideoSet>() {
            @Override
            public int compare(VideoSet o1, VideoSet o2) {
                if (o1.fullAmount!=o2.fullAmount) return Long.compare(o1.fullAmount, o2.fullAmount);
                if (o1.fullDuration!=o2.fullDuration) return Integer.compare(o1.fullDuration, o2.fullDuration);
                if (o1.videoSet.size()!=o2.videoSet.size()) return Integer.compare(o1.videoSet.size(), o2.videoSet.size());
                if (o1.hashCode() > o2.hashCode()) return 1;
                return -1;
            }
        });
        return sets.get(sets.size()-1).videoSet;
    }

    public void processVideos() {

        List<Advertisement> list = storage.list();
        ArrayList<VideoSet> sets = new ArrayList<>();
        Iterator<Advertisement> it = list.iterator();
        while (it.hasNext()) {
            Advertisement v = it.next();
            if (v.getHits()<=0 || v.getDuration()>timeSeconds) it.remove();
        }
        List<Advertisement> aVideos = searchVideo(list,sets, 0, 1 << list.size());

        if(aVideos.isEmpty())throw new NoVideoAvailableException();

        Collections.sort(aVideos, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                long a = o1.getAmountPerOneDisplaying();
                long b = o2.getAmountPerOneDisplaying();
                return (Long.compare(a, b)!=0) ? Long.compare(a, b) :
                        Long.compare(a * 1000 / o1.getDuration(), b * 1000 / o2.getDuration());
            }
        });

        for (Advertisement v:aVideos) {
            long a = v.getAmountPerOneDisplaying();
            ConsoleHelper.writeMessage(String.format("%s Video is displaying... %s, %s", v.getName(), a, a*1000/v.getDuration()));
            v.revalidate();
        }
    }
}
