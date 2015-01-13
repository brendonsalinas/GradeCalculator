package com.lazyengineers.gradecalculator.utils;

import android.content.Context;

import com.lazyengineers.gradecalculator.storage.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class dbUtils {
    // filename
    final static private String fileName = "yearList.store";

    /* Save/Write methods */
    public static void save(ArrayList<yearStorage> data, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<yearStorage> read(Context context) {
        ArrayList<yearStorage> instance;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            instance = (ArrayList<yearStorage>) is.readObject();
            is.close();
        } catch (Exception e) {
            // handle app first boot when file is not yet created
            // create new data instance
            instance = new ArrayList<yearStorage>();
            // create initial entries.
            instance.add(new yearStorage("Fall 2014"));
            instance.get(0).getCourseList().add(new courseStorage("Writing 39C", 4, "A"));
        }
        return instance;
    }

}
