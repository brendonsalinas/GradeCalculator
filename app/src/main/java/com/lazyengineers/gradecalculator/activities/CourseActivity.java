package com.lazyengineers.gradecalculator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lazyengineers.gradecalculator.R;
import com.lazyengineers.gradecalculator.storage.*;
import com.lazyengineers.gradecalculator.utils.dbUtils;

import java.util.ArrayList;

public class CourseActivity extends Activity {

    // we need this to serialize the data.  Avoid using except for load and save.
    private ArrayList<yearStorage> yearsList;

    // main instance data.
    private ArrayList<courseStorage> coursesList;

    private int yearPosition;

    // adapter should be accessible class wide.
    private coursesArrayAdapter adapter;

    // listView should be accessible class wide for now
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent courseIntent = getIntent();
        // the year index that was clicked in the list view
        yearPosition = courseIntent.getIntExtra("pos",-1);
        loadCourse(); // TODO: handle this?

        // set actionbar label
        setTitle(yearsList.get(yearPosition).getLabel());

        setContentView(R.layout.activity_course);

        // create listview
        adapter = new coursesArrayAdapter(this, coursesList);

        // setup adapter
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
    }

    private class coursesArrayAdapter extends ArrayAdapter<courseStorage> {

        public coursesArrayAdapter(Context context, ArrayList<courseStorage> courses) {
            super(context, 0, courses);
        }

        @Override
        public int getCount() {
            return coursesList.size();
        }
        @Override
        public courseStorage getItem(int pos) {
            return coursesList.get(pos);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make position accessible for inner classes
            final int pos = position;
            courseStorage course = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.courses_layout, parent, false);
            }

            // TODO: stub: add items here

            // Return the completed view to render on screen
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
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
            case R.id.action_clear:
                confirmationDialog();
                return true;
            case R.id.action_refresh:
                adapter.notifyDataSetChanged();
                return true;
            case R.id.action_add_icon:
                //setYearLabelDialog(0, true);
                return true;
            case R.id.action_add_text:
                // same as action_add_icon.
                return true;
            default:
                System.out.println("Default case reached.");
        }
        return false;
    }

    private void confirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(R.string.year_confirmation_title);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.year_confirmation_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, delete list
                        clearEntries();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.btn_cancel,new DialogInterface.OnClickListener() {
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

    private void clearEntries() {
        coursesList = new ArrayList<courseStorage>();
        adapter.notifyDataSetChanged();
    }

    private void loadCourse() {
        yearsList = dbUtils.read(this);

        // required for list View
        coursesList = yearsList.get(yearPosition).getCourseList();
    }

    protected void onResume() {
        loadCourse();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dbUtils.save(yearsList, this);
        super.onPause();
    }


}
