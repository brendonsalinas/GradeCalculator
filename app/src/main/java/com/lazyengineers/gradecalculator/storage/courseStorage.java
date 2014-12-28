package com.lazyengineers.gradecalculator.storage;

public class courseStorage {
    private int units;
    private String grade, name;

    public courseStorage() {
        // this should never be called
        units = 0;
        grade = "";
        name = "";
    }

    public void setInfo(String n, int u, String g) {
        units = u;
        grade = g;
        name = n;
    }
}