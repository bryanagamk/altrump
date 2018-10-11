package com.pens.afdolash.altrump.information.machine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.navbar.MainActivity;
import com.pens.afdolash.altrump.splash.SignInActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class MachineAddActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText et_idMesin, et_namaMesin, et_mobilNew, et_mobilRefill, et_motorNew, et_motorRefill;
    HashMap<String, String> price;
    String idMesin, namaMesin;

    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference db;
    String user_key;

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

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MachineAddActivity.this, SignInActivity.class));
                    finish();
                } else {
                    final String email = user.getEmail();
                    db = FirebaseDatabase.getInstance().getReference();

                    db.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Users user = postSnapshot.getValue(Users.class);
                                if (email.equals(user.getEmail())){
                                    user_key = postSnapshot.getKey();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

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

                Locale locale = Locale.getDefault();
                TimeZone timeZone = TimeZone.getDefault();
                Calendar tanggal = Calendar.getInstance(timeZone, locale);
                Machine machine = new Machine(idMesin, namaMesin, price, tanggal.getTime().toString(), user_key);
                if (!idMesin.isEmpty() && !namaMesin.isEmpty() ){
                    mDatabase.child("machine").child(idMesin).setValue(machine);
                    finish();
                }
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }
}
