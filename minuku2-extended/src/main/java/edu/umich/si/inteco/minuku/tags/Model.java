package edu.umich.si.inteco.minuku.tags;

import com.google.common.collect.EvictingQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by neerajkumar on 10/2/16.
 */

public class Model {
    private static Model instance = null;

    private static int TOP_N = 5;

    private Map<String, Integer> tagCountMap;
    private EvictingQueue<String> mostRecentTags = EvictingQueue.create(TOP_N);

    private String[] defaultTags = new String[] {"Tag1", "Tag2", "Tag3", "Tag4", "Tag5",
            "Tag6", "Tag7", "Tag8", "Tag9", "Tag10",};

    private Model() {
        // Get information from DAO here.
        tagCountMap = new HashMap<>();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public void incrementTagCount(String tag) {
        if(!tagCountMap.containsKey(tag)) {
            tagCountMap.put(tag, 0);
        }
        mostRecentTags.add(tag);
        tagCountMap.put(tag, tagCountMap.get(tag) + 1);
        // Update DAO here.
    }

    public String[] getTags(){
        String[] tags = new String[tagCountMap.keySet().size()];
        return tagCountMap.keySet().toArray(tags);
    }

    public String[] getRelevantTags() {
        String[] mostRelevantTags = new String[TOP_N * 2];
        Set<String> mostRelevantTagsSet = new HashSet<>();
        mostRelevantTagsSet.addAll(getMostRecentTags());
        mostRelevantTagsSet.addAll(tagsSortedByUsage());

        // Add default tags if we don't have enough tags to show.
        // Doing it one by one as we are adding to a set and there
        // might be times when a defaultTag is also a most recent or
        // most used tag and adding that won't affect the size of the
        // set.
        for(String tag: defaultTags) {
            if(mostRelevantTagsSet.size() >= TOP_N * 2) {
                break;
            }
            mostRelevantTagsSet.add(tag);
        }
//        if(mostRelevantTagsList.size() < TOP_N * 2) {
//            int openSlotCount = TOP_N*2 - mostRelevantTagsList.size();
//            mostRelevantTagsList.addAll(Arrays.asList(defaultTags).subList(0, openSlotCount));
//        }
        mostRelevantTagsSet.toArray(mostRelevantTags);
        return mostRelevantTags;
    }


    private  List<String> tagsSortedByUsage() {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(tagCountMap.entrySet());
        List<String> result = new LinkedList<>();
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                Integer i1 = ((Map.Entry<String, Integer>) (o1)).getValue();
                Integer i2 = ((Map.Entry<String, Integer>) (o2)).getValue();
                return i1.compareTo(i2);
            }
        });

        for(Map.Entry<String, Integer> entry: list) {
            result.add(entry.getKey());
        }
        return result;
    }

    private List<String> getMostRecentTags() {
        List<String> recentTags = new LinkedList<>();
        recentTags.addAll(mostRecentTags);
        return recentTags;
    }

}
