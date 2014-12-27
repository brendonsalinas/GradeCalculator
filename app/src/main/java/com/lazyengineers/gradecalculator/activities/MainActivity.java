package com.lazyengineers.gradecalculator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import com.lazyengineers.gradecalculator.storage.*;
import com.lazyengineers.gradecalculator.utils.*;

import com.lazyengineers.gradecalculator.R;

/*
    This should display all the years in a listview
 */

// default is listActivity
public class MainActivity extends Activity {
    // filename
    final static private String fileName = "yearList.store";

    // instance
    private ArrayList<yearStorage> yearsList;

    // adapter should be accessible class wide.
    private yearsArrayAdapter adapter;

    // listView should be accessible class wide for now
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load data
        yearsList = dbUtils.read(fileName, this);

        // create listview
        adapter = new yearsArrayAdapter(this, yearsList);

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        // TODO: this should be a popup menu
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final yearStorage item = (yearStorage) parent.getItemAtPosition(position);
                yearsList.remove(item);
                adapter.notifyDataSetChanged();
                view.setAlpha(1);
            }
        });

    }

    private class yearsArrayAdapter extends ArrayAdapter<yearStorage> {

        public yearsArrayAdapter(Context context, ArrayList<yearStorage> years) {
            super(context, 0, years); // figure out what this does...
        }

        @Override
        public int getCount() {
            return yearsList.size();
        }
        @Override
        public yearStorage getItem(int pos) {
            return yearsList.get(pos);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            yearStorage year = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.years_layout, parent, false);
            }
            // Lookup view for data population
            TextView quartertv  = (TextView) convertView.findViewById(R.id.quarter);
            TextView yeartv = (TextView) convertView.findViewById(R.id.year);
            // Populate the data into the template view using the data object
            quartertv.setText(year.getQuarter());
            yeartv.setText(year.getYear());
            // Return the completed view to render on screen
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;
            case R.id.action_clear:
                confirmationDialog();
                return true;
            case R.id.action_refresh:
                System.out.println("Refresh case clicked!");
                adapter.notifyDataSetChanged();
                return true;
            default:
                System.out.println("Default case reached.");
        }
        return false;
    }

    private void confirmationAction(boolean yes) {
        if (yes) {
            yearsList = new ArrayList<yearStorage>();
            adapter.notifyDataSetChanged();
        }
    }

    private void confirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Delete All Data?");

        // set dialog message
        alertDialogBuilder
                .setMessage("This action will delete all saved years, courses, and grades.")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, delete list
                        confirmationAction(true);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    // database implementation here
    /*
     should have Year and Course Activity to handle the UI.  We should initially populate the Years
      and Courses by reading the database and generating a listview.

      Initially, here, we call year activity and display them through databases
      how will we handle shuffling the years around?  have saved indexes?  don't worry about that yet

      Then, under year, it will display the classes.  Action bar should change text depending on which
      activity we are in.
      */

    @Override
    protected void onPause() {
        // save instance data
        if (yearsList.size() == 0) {
            // reload data on close for testing.
            yearsList.add(new yearStorage("Summer","2013"));
            yearsList.add(new yearStorage("Fall","2014"));
            yearsList.add(new yearStorage("Spring","2014"));
            yearsList.add(new yearStorage("Winter","2010"));
        }

        dbUtils.write(yearsList, fileName, this);
        super.onPause();
    }
}
