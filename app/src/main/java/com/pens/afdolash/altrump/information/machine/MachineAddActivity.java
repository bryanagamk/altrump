package com.pens.afdolash.altrump.information.machine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class MachineAddActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText et_idMesin, et_namaMesin, et_mobilNew, et_mobilRefill, et_motorNew, et_motorRefill;
    HashMap<String, String> price;
    String idMesin, namaMesin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Mesin");

        et_idMesin = findViewById(R.id.et_idMesin);
        et_namaMesin = findViewById(R.id.et_namaMesin);
        et_mobilNew = findViewById(R.id.et_mobilNew);
        et_mobilRefill = findViewById(R.id.et_mobilRefill);
        et_motorNew = findViewById(R.id.et_motorNew);
        et_motorRefill = findViewById(R.id.et_motorRefill);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
            case android.R.id.home :
                finish();
                break;
            case R.id.action_save :
                // Code to add data
                price = new HashMap<String, String>();
                price.put("MobilNew", et_mobilNew.getText().toString());
                price.put("MobilRefill", et_mobilRefill.getText().toString());
                price.put("MotorNew", et_motorNew.getText().toString());
                price.put("MotorRefill", et_motorRefill.getText().toString());

                idMesin = et_idMesin.getText().toString();
                namaMesin = et_namaMesin.getText().toString();

                String userUID = FirebaseAuth.getInstance().getUid();
                Locale locale = Locale.getDefault();
                TimeZone timeZone = TimeZone.getDefault();
                Calendar tanggal = Calendar.getInstance(timeZone, locale);
                Machine machine = new Machine(idMesin, namaMesin, price, tanggal.getTime().toString(), userUID);
                if (!idMesin.isEmpty() && !namaMesin.isEmpty() ){
                    mDatabase.child("machines").child(idMesin).setValue(machine);
                    finish();
                }
                break;
        }

        return true;
    }
}
