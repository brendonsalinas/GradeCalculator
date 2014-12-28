package com.lazyengineers.gradecalculator.utils;

import android.content.Context;

import com.lazyengineers.gradecalculator.storage.yearStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class dbUtils {

    /* Save/Write methods */
    public static void save(ArrayList<yearStorage> data, String fileName, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<yearStorage> read(String fileName, Context context) {
        ArrayList<yearStorage> instance;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            instance = (ArrayList<yearStorage>) is.readObject();
            is.close();
        } catch (Exception e) {
            instance = new ArrayList<yearStorage>();
        }
        return instance;
    }

}
