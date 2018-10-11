package com.pens.afdolash.altrump.report.transaction;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.splash.SignInActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportTransactionYearFragment extends Fragment {

    private MaterialSpinner spinnerYear;

    DatabaseReference db;
    List<DataDevice> dataDevices;
    long date;
    int count;
    int total;
    int year;
    Calendar input;
    int countDays = 0;
    int countMonths = 0;
    int totalMonths = 0;
    String user_key;
    String machineID;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;
    String[] tahun;

    TextView tv_transaction, tv_income;

    public ReportTransactionYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_transaction_year, container, false);

        tv_transaction = view.findViewById(R.id.tv_transaction);
        tv_income = view.findViewById(R.id.tv_income);


        db = FirebaseDatabase.getInstance().getReference();

        spinnerYear = (MaterialSpinner) view.findViewById(R.id.spinner_year);
        spinnerYear.setItems(getResources().getStringArray(R.array.year_list));
        spinnerYear.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat_regular), Typeface.NORMAL);

        tahun = getResources().getStringArray(R.array.year_list);
        year = Integer.parseInt(tahun[spinnerYear.getSelectedIndex()]);

        input = Calendar.getInstance();
        input.set(year, 0, 1);
        input = setTimming(input);
        getAuth();

        spinnerYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                year = Integer.parseInt(tahun[view.getSelectedIndex()]);
                input.set(year, 0, 1);
                input = setTimming(input);
                getMachine();
            }
        });

        return view;
    }

    public void getData(String machineID){

        db.child(machineID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.set(year, 0, 1);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Device data = postSnapshot.getValue(Device.class);

                    try {
                        Date dates = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(dates);
                        String val = data.getPrice();
                        val = val.replace(",", "");
                        int pay = Integer.parseInt(val);

                        if (data.getStatusA().equals("1")){
                            if (dateChoosen.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                                totalMonths += (pay * 500);
                                countMonths++;
                            }
                        }

                        String day = Integer.toString(countDays);
                        String months = Integer.toString(countMonths);
                        String price = Integer.toString(totalMonths);
                        tv_income.setText("Rp. " + price);
                        tv_transaction.setText(months);

                    } catch (Exception ignored) {

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

                countDays = 0;
                countMonths = 0;
                totalMonths = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Machine machine = postSnapshot.getValue(Machine.class);
                    try {
                        if (machine != null && user_key.equals(machine.getUser_key())) {
                            //adding machine to the list
                            machineID = machine.getId_mesin();
                            getData(machineID);
                        }
                    } catch (Exception e){

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
