package com.pens.afdolash.altrump.report.machine;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.dashboard.MachineList;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.splash.SignInActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportMachineFragment extends Fragment {
    long date;

    private static final String TAG = "ReportCuk";

    DatabaseReference db;
    CalendarView cv_date;
    Calendar input;

    String status = "MATI";
    String jamHidup = "00:00:00", jamMati = "00:00:00";
    String user_key;
    String machineID;

    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;

    TextView tv_jamHidup, tv_jamMati;

    public ReportMachineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_machine, container, false);

        RelativeLayout gotoMachineDetail = view.findViewById(R.id.detailMesin);
        tv_jamHidup = view.findViewById(R.id.tv_jamHidup);
        tv_jamMati = view.findViewById(R.id.tv_jamMati);

        input = Calendar.getInstance();

        cv_date = view.findViewById(R.id.cv_calendar_date);
        date = cv_date.getDate();
        Log.d(TAG, "onCreateView: date " + date);
        getAuth();

        cv_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull final CalendarView calendarView, int i, int i1, int i2) {

                input.set(i, i1, i2);
                input = setTimming(input);
                date = input.getTimeInMillis();
                Log.d(TAG, "onSelectedDayChange: date " + date);
                getMachine();
                Log.d(TAG, "onSelectedDayChange: " + i + " " + i1 + " " + i2);
            }
        });

        gotoMachineDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportMachineDetailActivity.class);
                intent.putExtra("tgl", date);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getAuth(){
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), SignInActivity.class));
                } else {
                    final String email = user.getEmail();
                    db = FirebaseDatabase.getInstance().getReference();

                    db.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Users user = postSnapshot.getValue(Users.class);
                                if (email.equals(user.getEmail())){
                                    //TODO: ganti user key yg sesuai
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

    public void getMachine(){

        db.child("machine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> jamHidupArray = new ArrayList<>();
                final ArrayList<String> jamMatiArray = new ArrayList<>();

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

                                Calendar dateChoosen = Calendar.getInstance();
                                dateChoosen.setTimeInMillis(date);
                                Calendar date = Calendar.getInstance();
                                long dataTime = 0;

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    objDevice = postSnapshot.getValue(Device.class);

                                    try {
                                        String input = objDevice.getDate() + " " + objDevice.getTime();
                                        Date var = formatter.parse(input);
                                        date.setTime(var);
                                        if (dateChoosen.get(Calendar.MONTH) == date.get(Calendar.MONTH) && dateChoosen.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
                                            if (dateChoosen.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
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
                                    jamHidupArray.add(dataJamHidup.get(0));
//                                    machine.setJamHidup(dataJamHidup.get(0));
                                    jamMatiArray.add(dataJamHidup.get(dataJamHidup.size() - 1));
//                                    machine.setJamMati(dataJamHidup.get(dataJamHidup.size() - 1));
                                    long dif = dateChoosen.getTimeInMillis() - date.getTimeInMillis();
                                    if (dif > 1800000){
                                        status = "MATI";
                                    }
                                    machine.setStatus(status);
                                } else {
                                    machine.setJamHidup(jamHidup);
                                    machine.setJamMati(jamMati);
                                    machine.setStatus("MATI");
                                }
                                if (!jamHidupArray.isEmpty()){
                                    tv_jamHidup.setText(jamHidupArray.get(0));
                                    tv_jamMati.setText(jamHidupArray.get(jamHidupArray.size() - 1));
                                } else {
                                    tv_jamHidup.setText(jamHidup);
                                    tv_jamMati.setText(jamMati);
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
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    public Calendar setTimming(Calendar date) {
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);

        return date;
    }

}
