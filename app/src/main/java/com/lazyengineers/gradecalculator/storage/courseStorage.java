package com.lazyengineers.gradecalculator.storage;

// this should be a strictly storage implementation, helper functions go into util.

/**
 * Created by jianc on 12/23/14.
 */
public class courseStorage {
    private int units;
    private String grade, name;

    public courseStorage() {
        // this should never be called
        units = 0;
        grade = "";
        name = "";
    }
/*
    public courseStorage clone() {
        courseStorage c = new courseStorage();
        c.setInfo(this.name,this.units,this.grade);
        return c;
    }
*/

    public void setInfo(String n, int u, String g) {
        units = u;
        grade = g;
        name = n;
    }
}