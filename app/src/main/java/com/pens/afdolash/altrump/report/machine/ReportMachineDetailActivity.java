package com.pens.afdolash.altrump.report.machine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.ImageHelper;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportMachineDetailActivity extends AppCompatActivity {

    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;
    private static final String TAG = "ReportCukdetail";
    ListView listViewMachine;
    List<Machine> machines;
    // Get a reference to your user
    DatabaseReference db;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;
    String user_key;
    String machineID;
    MachineList machineAdapter;
    String status = "MATI";
    String jamHidup = "00:00:00", jamMati = "00:00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_machine_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBlur = (ImageView) findViewById(R.id.img_blur);
        view = findViewById(R.id.container);

        imgBlur.post(new Runnable() {
            @Override
            public void run() {
                blurBitmap = createBlurBitmap();
                imgBlur.setImageBitmap(blurBitmap);
            }
        });
        Intent intent = getIntent();
        long date = intent.getLongExtra("tgl",0);
        Log.d(TAG, "onCreate: date " + date);

        db = FirebaseDatabase.getInstance().getReference();

        listViewMachine = view.findViewById(R.id.listViewMachine);
        machines = new ArrayList<>();
        getAuth();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    public void getAuth() {
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String email = user.getEmail();
                    db = FirebaseDatabase.getInstance().getReference();

                    db.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Users user = postSnapshot.getValue(Users.class);
                                if (email.equals(user.getEmail())) {
                                    user_key = "-LM2S1zRn_pUW65vpclQ";
                                    getMachine();
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

    public void getMachine() {

        db.child("machine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                machines.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Machine machine = postSnapshot.getValue(Machine.class);

                    if (machine != null && user_key.equals(machine.getUser_key())) {
                        machineID = machine.getId_mesin();
                        DatabaseReference dbDevice = FirebaseDatabase.getInstance().getReference().child(machineID);
                        dbDevice.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                                Device objDevice = null;
                                long diff = 0;
                                ArrayList<String> dataJamHidup = new ArrayList<>();
                                ArrayList<String> dataJamMati = new ArrayList<>();
                                Calendar now = Calendar.getInstance();
                                Calendar date = Calendar.getInstance();
                                long dataTime = 0;

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    objDevice = postSnapshot.getValue(Device.class);

                                    try {
                                        String input = objDevice.getDate() + " " + objDevice.getTime();
                                        Date var = formatter.parse(input);
                                        date.setTime(var);
                                        if (now.get(Calendar.MONTH) == date.get(Calendar.MONTH) && now.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
                                            if (16 == date.get(Calendar.DAY_OF_MONTH)) {
                                                long currentTime = date.getTimeInMillis();

                                                if (dataTime != 0){
                                                    diff = currentTime - dataTime;
                                                } else {
                                                    diff = 0;
                                                }

                                                if ((objDevice.getStatusA().equals("3") && objDevice.getStatusB().equals("3") && objDevice.getType().equals("3")
                                                        && objDevice.getPrice().equals("0")) || (diff < 180000)) {
                                                    status = "HIDUP";
                                                    dataJamHidup.add(objDevice.getTime());
                                                } else {
                                                    status = "MATI";
                                                    dataJamMati.add(objDevice.getTime());
                                                }
                                                dataTime = currentTime;
                                            }
                                        }

                                    } catch (ParseException ignored) {
                                    }
                                }

                                if (!dataJamHidup.isEmpty() && !dataJamMati.isEmpty()) {
                                    machine.setJamHidup(dataJamHidup.get(0));
                                    machine.setJamMati(dataJamHidup.get(dataJamHidup.size() - 1));
                                    machine.setDate(objDevice.getDate());
                                    long dif = now.getTimeInMillis() - date.getTimeInMillis();
                                    if (dif > 1800000){
                                        status = "MATI";
                                    }
                                    machine.setStatus(status);
                                } else {
                                    machine.setJamHidup(jamHidup);
                                    machine.setJamMati(jamMati);
                                    machine.setDate(objDevice.getDate());
                                    machine.setStatus("MATI");
                                }

                                machines.add(machine);

                                if (getApplicationContext() != null) {
                                    machineAdapter = new MachineList(ReportMachineDetailActivity.this, machines);
                                    listViewMachine.setAdapter(machineAdapter);
                                    machineAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public Bitmap createBlurBitmap() {
        Bitmap bitmap = captureView(view);
        if (bitmap != null) {
            ImageHelper.blurBitmapWithRenderscript(
                    RenderScript.create(this),
                    bitmap);
        }

        return bitmap;
    }

    public Bitmap captureView(View view) {
        // Create a Bitmap with the same dimensions as the View
        Bitmap image = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality

        // Draw the view inside the Bitmap
        Canvas canvas = new Canvas(image);
        view.draw(canvas);

        // Make it frosty
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        // ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000); // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(image, 0, 0, paint);

        return image;
    }
}
