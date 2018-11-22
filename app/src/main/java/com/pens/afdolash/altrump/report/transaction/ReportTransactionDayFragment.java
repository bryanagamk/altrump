package com.pens.afdolash.altrump.report.transaction;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
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
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Item;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.splash.SignInActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportTransactionDayFragment extends Fragment {

    long date;
    int countDay, i, total;
    String day;
    String months;
    String price;
    ArrayList<Integer> countTrc;
    private static final String TAG = "ReportDay";

    DatabaseReference db;
    CalendarView cv_date;
    Calendar input;

    int countDays = 0;
    int countMonths = 0;
    int totalMonths = 0;
    String user_key;
    String machineID, tv_namaSPBU, tv_jmlPbaruAtas, tv_jmlTBerhasil, tv_jmlTGagal, tv_jmlPbaru, tv_jmlPulang;

    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;

    TextView tv_transaction, tv_income;

    public ReportTransactionDayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_transaction_day, container, false);
        db = FirebaseDatabase.getInstance().getReference();

        tv_transaction = view.findViewById(R.id.tv_transaction);
        tv_income = view.findViewById(R.id.tv_income);

        input = Calendar.getInstance();

        try {
            day = getArguments().getString("day");
            price = getArguments().getString("price");

            tv_income.setText("Rp. " + price);
            tv_transaction.setText(day);

        } catch (Exception e) {
            tv_income.setText("Rp. 0");
            tv_transaction.setText("0");
        }

        cv_date = view.findViewById(R.id.cv_calendar_date);
        date = cv_date.getDate();
        getAuth();

        cv_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull final CalendarView calendarView, int i, int i1, int i2) {

                input.set(i, i1, i2);
                input = setTimming(input);
                date = input.getTimeInMillis();
                getMachine();
                Log.d(TAG, "onSelectedDayChange: " + i + " " + i1 + " " + i2);
            }
        });

        RelativeLayout relativeLayout = view.findViewById(R.id.detailTransaksi);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportTransactionDetailActivity.class);
                intent.putExtra("jumlah", day);
                intent.putExtra("income", price);
                intent.putExtra("tahun", input.get(Calendar.YEAR));
                intent.putExtra("bulan", input.get(Calendar.MONTH));
                intent.putExtra("tgl", input.get(Calendar.DAY_OF_MONTH));
                intent.putExtra("array", countTrc);
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

        db.child("machine").addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void getData(String machineID){

        db.child(machineID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.setTimeInMillis(date);
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
                            if (dateChoosen.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && dateChoosen.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                                if (dateChoosen.get(Calendar.DAY_OF_YEAR) == myCal.get(Calendar.DAY_OF_YEAR)) {

                                    countDays++;
                                    totalMonths += (pay * 500);
                                }
                                countMonths++;
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }



                String day = Integer.toString(countDays);
                String months = Integer.toString(countMonths);
                String price = Integer.toString(totalMonths);

                String format = "###,###.##";
                DecimalFormat dm = new DecimalFormat(format);
                DecimalFormat currency = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator('.');
                formatRp.setGroupingSeparator('.');

                currency.setDecimalFormatSymbols(formatRp);
                tv_income.setText(String.valueOf(currency.format(totalMonths)));
                tv_transaction.setText(dm.format(countDays));

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
