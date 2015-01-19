package com.lazyengineers.gradecalculator.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lazyengineers.gradecalculator.R;
import com.lazyengineers.gradecalculator.storage.*;
import com.lazyengineers.gradecalculator.utils.dbUtils;

import java.util.ArrayList;
import java.util.Arrays;

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // make position accessible for inner classes
            courseStorage course = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.courses_layout, parent, false);
            }

            // Lookup view for data population

            // populate label
            TextView courseLabel  = (TextView) convertView.findViewById(R.id.course_label);
            courseLabel.setText(course.getLabel());

            // populate grade
            TextView courseGrade = (TextView) convertView.findViewById(R.id.course_grade);
            courseGrade.setText(course.getGrade());

            // populate units
            TextView courseUnits = (TextView) convertView.findViewById(R.id.course_units);
            courseUnits.setText(Integer.toString(course.getUnits()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemPressDialog(position, view);
                }
            });

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
                setCourseInfoDialog(0, true);
                return true;
            case R.id.action_add_text:
                // same as action_add_icon.
                setCourseInfoDialog(0, true);
                return true;
            case android.R.id.home:
                dbUtils.save(yearsList, CourseActivity.this);
                finish();
                return true;
        }
        return false;
    }

    /* -- Helper Methods -- */
    private void removeCourse(int position, View view) {
        coursesList.remove(position);
        view.setAlpha(1);
        adapter.notifyDataSetChanged();
    }

    /* -- Dialog Boxes -- */
    private void setCourseInfoDialog(final int pos, final boolean append) {
        // should get LABEL, grade, units.
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
        builder.setTitle(R.string.course_input_title);

        // Set up the input
        LayoutInflater inflater = CourseActivity.this.getLayoutInflater();

        final View dialogLayout = inflater.inflate(R.layout.dialog_courseinput, null);

        // setup courseLabel
        final EditText courseLabel = (EditText) dialogLayout.findViewById(R.id.course_label_input);
        // if entry already exists, and mode is edit, populate input with this for user to modify
        if (!append)
            courseLabel.setText(coursesList.get(pos).getLabel());

        // setup gradesPicker
        final NumberPicker gradePicker = (NumberPicker) dialogLayout.findViewById(R.id.course_grade_picker);
        // export this to resource?
        final String grades[] = new String[] { "A+","A","A-","B+","B","B-","C+","C","C-","D+","D","D-","F" };
        gradePicker.setMinValue(0);
        gradePicker.setMaxValue(grades.length-1);
        // if entry already exists, and mode is edit, populate input with this for user to modify
        if (!append)
            gradePicker.setValue(Arrays.asList(grades).indexOf(coursesList.get(pos).getGrade()));
        gradePicker.setWrapSelectorWheel(false);
        gradePicker.setDisplayedValues(grades);

        // setup unitsPicker
        final NumberPicker unitsPicker = (NumberPicker) dialogLayout.findViewById(R.id.course_units_picker);
        unitsPicker.setMinValue(0);
        unitsPicker.setMaxValue(8);
        // if entry already exists, and mode is edit, populate input with this for user to modify
        if (!append)
            unitsPicker.setValue(coursesList.get(pos).getUnits());
        unitsPicker.setWrapSelectorWheel(false);

        // Inflate view (perhaps do the same for all other dialogBoxes
        builder.setView(dialogLayout);

        builder.setPositiveButton(R.string.btn_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = courseLabel.getText().toString();
                String grade = grades[gradePicker.getValue()];
                int units = unitsPicker.getValue();
                if (append) {
                    addCourse(text, units, grade);
                } else {
                    modifyCourseInfo(pos, text, units, grade);
                }
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

        if (append)
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

        // Now set the textchange listener for edittext
        courseLabel.addTextChangedListener(new TextWatcher() {
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
    private void onItemPressDialog(final int pos, final View view) {
        AlertDialog.Builder chooser = new AlertDialog.Builder(this);

        chooser.setTitle(R.string.menu_title);

        // set dialog message
        chooser
                .setCancelable(true)
                .setItems(R.array.item_press_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        // position = index of item clicked
                        if (position == 0) {
                            setCourseInfoDialog(pos, false);
                        } else if (position == 1) {
                            removeCourse(pos, view);
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
        alertDialogBuilder.setTitle(R.string.course_confirmation_title);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.course_confirmation_msg)
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

    private void addCourse(String label, int units, String grades) {
        coursesList.add( new courseStorage(label,units,grades));
        adapter.notifyDataSetChanged();
    }

    private void modifyCourseInfo(int pos, String label, int units, String grades) {
        coursesList.get(pos).setInfo(label,units,grades);
        adapter.notifyDataSetChanged();
    }

    private void clearEntries() {
        coursesList.clear();
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
    public void onBackPressed() {
        dbUtils.save(yearsList, this);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        dbUtils.save(yearsList, this);
        super.onPause();
    }


}
