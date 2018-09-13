package com.pens.afdolash.altrump.information.employee;

import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pens.afdolash.altrump.R;

public class EmployeeUpdateActivity extends AppCompatActivity {

    private MaterialSpinner spinnerWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Pegawai");

        spinnerWork = (MaterialSpinner) findViewById(R.id.spinner_work);
        spinnerWork.setItems(getResources().getStringArray(R.array.machine_list));
        spinnerWork.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_regular), Typeface.NORMAL);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save :
                // Code to update data
                break;
        }

        return true;
    }
}
