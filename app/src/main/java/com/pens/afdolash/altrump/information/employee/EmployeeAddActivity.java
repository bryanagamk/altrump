package com.pens.afdolash.altrump.information.employee;

import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pens.afdolash.altrump.R;

public class EmployeeAddActivity extends AppCompatActivity {

    private MaterialSpinner spinnerWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Pegawai");

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
                // Code to add data
                break;
        }

        return true;
    }
}
