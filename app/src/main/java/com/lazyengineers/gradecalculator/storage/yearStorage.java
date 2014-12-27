package com.lazyengineers.gradecalculator.storage;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
// serializable import


/**
 * Created by jianc on 12/23/14.
 */
public class yearStorage implements Serializable {
    private String quarter, year;
    private List<courseStorage> courses;

    public yearStorage() {
        // this should never be called.
        courses = new ArrayList<courseStorage>();
        quarter = year = "";
    }

    public yearStorage(String q, String y) {
        courses = new ArrayList<courseStorage>();
        quarter = q;
        year = y;
    }

    // deep copy, don't know when we'll need this.. yet...
/*
    public yearStorage clone() {
        yearStorage t = new yearStorage();
        t.modify(this.quarter,this.year);
        for (courseStorage c: courses) {
            t.courses.add(c.clone());
        }
        return t;
    }
*/

    public void modify(String q, String y) {
        quarter = q;
        year = y;
    }

    public void addCourse() {
        courses.add(new courseStorage());
    }

    public void modifyCourse(int index, String name, int units, String grade) {
        courses.get(index).setInfo(name,units,grade);
    }

    public void delCourse(int index) {
        // if we ever implement drag and drop, we should delete by object.. idk about this...
        courses.remove(index);
    }

    public String getQuarter() {
        return quarter;
    }

    public String getYear() {
        return year;
    }
}
