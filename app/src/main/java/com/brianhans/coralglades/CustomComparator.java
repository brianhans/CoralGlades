package com.brianhans.coralglades;

import java.util.Comparator;

import twitter4j.Status;

/**
 * Created by Brian on 11/28/2014.
 */
public class CustomComparator implements Comparator<Status> {
    @Override
    public int compare(Status o1, Status o2) {
        return o1.getCreatedAt().compareTo(o2.getCreatedAt()) * -1;
    }
}
