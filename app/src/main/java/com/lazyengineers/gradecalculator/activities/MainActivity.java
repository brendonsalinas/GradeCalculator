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
import android.widget.EditText;
import android.widget.Toast;
import android.text.InputType;
import java.util.List;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.text.Editable;


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
            // make position accessible for inner classes
            final int pos = position;
            yearStorage year = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.years_layout, parent, false);
            }
            // Lookup view for data population
            TextView yearLabel  = (TextView) convertView.findViewById(R.id.year_label);
            yearLabel.setText(year.getLabel());
            yearLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemPressDialog(pos, view);
                    // should spawn submenu?
                }
            });

            //image button click should trigger new activity, and pass on json bundle of current object
            //after it is finished, it will return the modified bundle and we will set the current index to it.

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
                adapter.notifyDataSetChanged();
                return true;
            case R.id.action_add_icon:
                setYearLabelDialog(0, true);
                return true;
            case R.id.action_add_text:
                // same as action_add_icon.
                setYearLabelDialog(0, true);
                return true;
            default:
                System.out.println("Default case reached.");
        }
        return false;
    }

    private void removeYear(int position, View view) {
        yearsList.remove(position);
        view.setAlpha(1);
        adapter.notifyDataSetChanged();
    }

    private void setYearLabel(int position, String str) {
        yearsList.get(position).setLabel(str);
        adapter.notifyDataSetChanged();
    }

    private void addYear(String str) {
        yearsList.add(new yearStorage(str));
        adapter.notifyDataSetChanged();
    }

    private void clearEntries() {
        yearsList = new ArrayList<yearStorage>();
        adapter.notifyDataSetChanged();
    }

    private void setYearLabelDialog(int pos, boolean mode) {
        final boolean append = mode;
        final int parentPos = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a Label");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Ex: Fall 2014");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (append)
                    addYear(text);
                else
                    setYearLabel(parentPos, text);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        // Now set the textchange listener for edittext
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if edittext is empty
                if (TextUtils.isEmpty(s)) {
                    // Disable ok button
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    // Something into edit text. Enable the button.
                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });
    }

    // todo: generic dialog builder
    private void onItemPressDialog(int pos, View view) {
        final View parentView = view;
        final int parentPos = pos;
        AlertDialog.Builder chooser = new AlertDialog.Builder(this);

        chooser.setTitle("Choose an Action");

        // set dialog message
        chooser
            .setCancelable(true)
            .setItems(R.array.years_item_press_array, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int position) {
                    // position = index of item clicked
                    if (position == 0) {
                        setYearLabelDialog(parentPos,false);
                    } else if (position == 1) {
                        removeYear(parentPos, parentView);
                    }
                    dialog.dismiss();
                }
            });

        // create alert dialog
        AlertDialog dialog = chooser.create();
        dialog.setCanceledOnTouchOutside(true);

        // show it
        dialog.show();
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
                        clearEntries();
                        dialog.dismiss();
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

    protected void onResume() {
        yearsList = dbUtils.read(fileName, this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        dbUtils.save(yearsList, fileName, this);
        super.onPause();
    }
}
