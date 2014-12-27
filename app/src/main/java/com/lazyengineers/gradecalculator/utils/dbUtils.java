package com.lazyengineers.gradecalculator.utils;

import android.content.Context;

import com.lazyengineers.gradecalculator.storage.yearStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianc on 12/26/14.
 */
public class dbUtils {
/*
    public static ArrayList<yearStorage> cloneList(ArrayList<yearStorage> toCopy) {
        ArrayList<yearStorage> newList = new ArrayList<yearStorage>();
        for (ArrayList<yearStorage> item: toCopy) {
            newList.add(item.clone());
        }
        return newList;
    }
*/

    /* Save/Write methods */
    public static void write(ArrayList<yearStorage> data, String fileName, Context context) {
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
        // TODO: Add error checking
        ArrayList<yearStorage> instance;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            instance = (ArrayList<yearStorage>) is.readObject();
            is.close();
            // need to catch individual exception
            // for now: if doesn't exist, then return new empty list.
        } catch (Exception e) {
            instance = new ArrayList<yearStorage>();
            // test cases
            // in the future, the quarters should be chosen from spinners
            instance.add(new yearStorage("Spring","2013"));
            instance.add(new yearStorage("Fall","2014"));
        }
        return instance;
    }

}
