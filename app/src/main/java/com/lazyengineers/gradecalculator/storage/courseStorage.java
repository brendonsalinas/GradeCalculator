package com.lazyengineers.gradecalculator.storage;

public class courseStorage {
    private int units;
    private String grade, label;

    public courseStorage(String l, int u, String g) {
        setInfo(l, u, g);
    }

    public void setInfo(String l, int u, String g) {
        units = u;
        grade = g;
        label = l;
    }

    public int getUnits() {
        return units;
    }

    public String getGrade() {
        return grade;
    }

    public String getLabel() {
        return label;
    }
}