package com.lazyengineers.gradecalculator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lazyengineers.gradecalculator.R;
import com.lazyengineers.gradecalculator.storage.yearStorage;
import com.lazyengineers.gradecalculator.utils.dbUtils;

import java.util.ArrayList;

public class MainActivity extends Activity {
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
        yearsList = dbUtils.read(this);

        // create listview
        adapter = new yearsArrayAdapter(this, yearsList);

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
    }

    private class yearsArrayAdapter extends ArrayAdapter<yearStorage> {

        public yearsArrayAdapter(Context context, ArrayList<yearStorage> years) {
            super(context, 0, years);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // make position accessible for inner classes
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
                    onItemPressDialog(position, view);
                }
            });

            ImageButton courseButton = (ImageButton) convertView.findViewById(R.id.course_button);
            courseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: stub
                    // put position in intent extras
                    // spawn new activity)
                    Intent courseIntent = new Intent(MainActivity.this, CourseActivity.class);
                    courseIntent.putExtra("pos",position);
                    MainActivity.this.startActivity(courseIntent);
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
        yearsList.clear();
        adapter.notifyDataSetChanged();
    }

    private void setYearLabelDialog(final int position, final boolean append) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.year_label_title);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.year_label_hint);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.btn_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (append)
                    addYear(text);
                else
                    setYearLabel(position, text);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
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

        chooser.setTitle(R.string.menu_title);

        // set dialog message
        chooser
            .setCancelable(true)
            .setItems(R.array.item_press_array, new DialogInterface.OnClickListener() {
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

    protected void onResume() {
        yearsList = dbUtils.read(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        dbUtils.save(yearsList, this);
        super.onPause();
    }
}
