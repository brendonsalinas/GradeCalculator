package com.lazyengineers.gradecalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    // TODO: implement database
    int i = 0;
    TextView newSchoolYear1, newSchoolYear2, newSchoolYear3, newSchoolYear4,newSchoolYear5,
            newSchoolYear6, newSchoolYear7, newSchoolYear8, newSchoolYear9, newSchoolYear10;
    Button yearAdded;
    private static final int YEAR_ADDED_INFO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newSchoolYear1 = (TextView) findViewById(R.id.newSchoolYear1);
        newSchoolYear2 = (TextView) findViewById(R.id.newSchoolYear2);
        newSchoolYear3 = (TextView) findViewById(R.id.newSchoolYear3);
        newSchoolYear4 = (TextView) findViewById(R.id.newSchoolYear4);
        newSchoolYear5 = (TextView) findViewById(R.id.newSchoolYear5);
        newSchoolYear6 = (TextView) findViewById(R.id.newSchoolYear6);
        newSchoolYear7 = (TextView) findViewById(R.id.newSchoolYear7);
        newSchoolYear8 = (TextView) findViewById(R.id.newSchoolYear8);
        newSchoolYear9 = (TextView) findViewById(R.id.newSchoolYear9);
        newSchoolYear10 = (TextView) findViewById(R.id.newSchoolYear10);

        if(savedInstanceState != null){
            int oldI = savedInstanceState.getInt("OLDNUM");
            i = oldI;

            String newSY1 = savedInstanceState.getString("NEWSY1");
            String newSY2 = savedInstanceState.getString("NEWSY2");
            String newSY3 = savedInstanceState.getString("NEWSY3");
            String newSY4 = savedInstanceState.getString("NEWSY4");
            String newSY5 = savedInstanceState.getString("NEWSY5");
            String newSY6 = savedInstanceState.getString("NEWSY6");
            String newSY7 = savedInstanceState.getString("NEWSY7");
            String newSY8 = savedInstanceState.getString("NEWSY8");
            String newSY9 = savedInstanceState.getString("NEWSY9");
            String newSY10 = savedInstanceState.getString("NEWSY10");

            newSchoolYear1.setText(newSY1);
            newSchoolYear2.setText(newSY2);
            newSchoolYear3.setText(newSY3);
            newSchoolYear4.setText(newSY4);
            newSchoolYear5.setText(newSY5);
            newSchoolYear6.setText(newSY6);
            newSchoolYear7.setText(newSY7);
            newSchoolYear8.setText(newSY8);
            newSchoolYear9.setText(newSY9);
            newSchoolYear10.setText(newSY10);
        }

        String spYear1 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY1","EMPTY");
        if (!spYear1.equals("EMPTY")){
            newSchoolYear1.setText(spYear1);
        }
        String spYear2 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY2","EMPTY");
        if (!spYear2.equals("EMPTY")){
            newSchoolYear2.setText(spYear2);
        }
        String spYear3 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY3","EMPTY");
        if (!spYear3.equals("EMPTY")){
            newSchoolYear3.setText(spYear3);
        }
        String spYear4 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY4","EMPTY");
        if (!spYear4.equals("EMPTY")){
            newSchoolYear4.setText(spYear4);
        }
        String spYear5 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY5","EMPTY");
        if (!spYear5.equals("EMPTY")){
            newSchoolYear5.setText(spYear5);
        }
        String spYear6 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY6","EMPTY");
        if (!spYear6.equals("EMPTY")){
            newSchoolYear6.setText(spYear6);
        }
        String spYear7 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY7","EMPTY");
        if (!spYear7.equals("EMPTY")){
            newSchoolYear7.setText(spYear7);
        }
        String spYear8 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY8","EMPTY");
        if (!spYear8.equals("EMPTY")){
            newSchoolYear8.setText(spYear8);
        }
        String spYear9 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY9","EMPTY");
        if (!spYear9.equals("EMPTY")){
            newSchoolYear9.setText(spYear9);
        }
        String spYear10 = getPreferences(Context.MODE_PRIVATE).getString("NEWSY10","EMPTY");
        if (!spYear10.equals("EMPTY")){
            newSchoolYear10.setText(spYear10);
        }
        int oldNumber = getPreferences(Context.MODE_PRIVATE).getInt("OLDNUM",0);
        if(oldNumber != 0){
            i = oldNumber;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("NEWSY1",newSchoolYear1.getText().toString());
        outState.putString("NEWSY2",newSchoolYear2.getText().toString());
        outState.putString("NEWSY3",newSchoolYear3.getText().toString());
        outState.putString("NEWSY4",newSchoolYear4.getText().toString());
        outState.putString("NEWSY5",newSchoolYear5.getText().toString());
        outState.putString("NEWSY6",newSchoolYear6.getText().toString());
        outState.putString("NEWSY7",newSchoolYear7.getText().toString());
        outState.putString("NEWSY8",newSchoolYear8.getText().toString());
        outState.putString("NEWSY9",newSchoolYear9.getText().toString());
        outState.putString("NEWSY10",newSchoolYear10.getText().toString());
        outState.putInt("OLDNUM",i);

        super.onSaveInstanceState(outState);
    }

    private void saveYear(){

        SharedPreferences.Editor spEditor = getPreferences(Context.MODE_PRIVATE).edit();
        spEditor .putString("NEWSY1",newSchoolYear1.getText().toString());
        spEditor.putString("NEWSY2",newSchoolYear2.getText().toString());
        spEditor.putString("NEWSY3",newSchoolYear3.getText().toString());
        spEditor.putString("NEWSY4",newSchoolYear4.getText().toString());
        spEditor.putString("NEWSY5",newSchoolYear5.getText().toString());
        spEditor.putString("NEWSY6",newSchoolYear6.getText().toString());
        spEditor.putString("NEWSY7",newSchoolYear7.getText().toString());
        spEditor.putString("NEWSY8",newSchoolYear8.getText().toString());
        spEditor.putString("NEWSY9",newSchoolYear9.getText().toString());
        spEditor.putString("NEWSY10",newSchoolYear10.getText().toString());
        spEditor.putInt("OLDNUM",i);

        spEditor.commit();
    }

    @Override
    protected void onStop() {

        saveYear();

        super.onStop();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newYear(View view) {
        final int result = 1;

        if(i==10){
            Toast toast = Toast.makeText(getApplicationContext(),"Cannot add more years",Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent getYearIntent = new Intent(this, NewYear.class);

        startActivityForResult(getYearIntent,result);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        i++;
        int newSchoolYear = 0;

        switch (i){
            case 1:
                newSchoolYear = R.id.newSchoolYear1;
                break;
            case 2:
                newSchoolYear = R.id.newSchoolYear2;
                break;
            case 3:
                newSchoolYear = R.id.newSchoolYear3;
                break;
            case 4:
                newSchoolYear = R.id.newSchoolYear4;
                break;
            case 5:
                newSchoolYear = R.id.newSchoolYear5;
                break;
            case 6:
                newSchoolYear = R.id.newSchoolYear6;
                break;
            case 7:
                newSchoolYear = R.id.newSchoolYear7;
                break;
            case 8:
                newSchoolYear = R.id.newSchoolYear8;
                break;
            case 9:
                newSchoolYear = R.id.newSchoolYear9;
                break;
            case 10:
                newSchoolYear = R.id.newSchoolYear10;
                break;
            default:

        }

        TextView addedYear = (TextView) findViewById(newSchoolYear);

        String yearBack = data.getStringExtra("newYear");

        addedYear.append(yearBack);
    }

    public void year1(View view) {
        if (i < 1){
            return;
        }

    }

    public void year2(View view) {
        if (i < 2){
            return;
        }
    }

    public void year3(View view) {
        if (i < 3){
            return;
        }
    }

    public void year4(View view) {
        if (i < 4){
            return;
        }
    }

    public void year5(View view) {
        if (i < 5){
            return;
        }
    }

    public void year6(View view) {
        if (i < 6){
            return;
        }
    }

    public void year7(View view) {
        if (i < 7){
            return;
        }
    }

    public void year8(View view) {
        if (i < 8){
            return;
        }
    }

    public void year9(View view) {
        if (i < 9){
            return;
        }
    }

    public void year10(View view) {
        if (i < 10){
            return;
        }
    }
}
