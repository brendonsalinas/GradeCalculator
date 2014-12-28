package com.lazyengineers.gradecalculator.storage;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class yearStorage implements Serializable {
    private String label;
    private List<courseStorage> courses;

    public yearStorage() {
        // this should never be called.
        courses = new ArrayList<courseStorage>();
        label = "Default";
    }

    public yearStorage(String lbl) {
        courses = new ArrayList<courseStorage>();
        label=lbl;
    }

    public void setLabel(String lbl) {
        label = lbl;
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

    public String getLabel() {
        return label;
    }
}
