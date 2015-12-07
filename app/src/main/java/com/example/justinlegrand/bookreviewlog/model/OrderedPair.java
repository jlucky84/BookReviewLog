package com.example.justinlegrand.bookreviewlog.model;

/**
 * Created by Justin.LeGrand on 12/4/2015.
 */
public class OrderedPair<String, V> implements Pair<String, V> {

    private String key;
    private V value;

    public OrderedPair(String key, V value) {
        this.key = key;
        this.value = value;
    }

    public String getKey()	{ return key; }
    public V getValue() { return value; }
}