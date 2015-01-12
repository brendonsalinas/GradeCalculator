package com.lazyengineers.gradecalculator.storage;

import java.io.Serializable;
import java.util.ArrayList;

public class yearStorage implements Serializable {
    private String label;
    public ArrayList<courseStorage> courses;

    public yearStorage(String lbl) {
        courses = new ArrayList<courseStorage>();
        label=lbl;
    }

    public void setLabel(String lbl) {
        label = lbl;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<courseStorage> getCourseList() {
        return courses;
    }
}
