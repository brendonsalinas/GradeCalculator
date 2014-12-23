package com.lazyengineers.gradecalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class NewYear extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_year);

        Spinner spinner1 = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.season_spinner);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                R.array.season_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }


    public void onDoneClick(View view) {

        Spinner yearPicked = (Spinner) findViewById(R.id.year_spinner);

        String year = yearPicked.getSelectedItem().toString();

        Spinner seasonPicked = (Spinner) findViewById(R.id.season_spinner);

        String season = seasonPicked.getSelectedItem().toString();

        Intent returnValues = new Intent();

        returnValues.putExtra("newYear",season+" "+year);

        setResult(RESULT_OK, returnValues);

        finish();

    }
}
