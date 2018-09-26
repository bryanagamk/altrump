package com.pens.afdolash.altrump.report.transaction;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.dashboard.MachineList;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;

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
public class ReportTransactionDayFragment extends Fragment {

    long date;
    int countDay;
    int total;

    DatabaseReference db;

    List<DataDevice> dataDevices;

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
        db.keepSynced(true);

        CalendarView cv_date = view.findViewById(R.id.cv_calendar_date);
        date = cv_date.getDate();
        cv_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = calendarView.getDate();
                Log.d(TAG, "onSelectedDayChange: onDataChange date :" + date);
                onStart();
            }
        });

        tv_transaction = view.findViewById(R.id.tv_transaction);
        tv_income = view.findViewById(R.id.tv_income);


        RelativeLayout relativeLayout = view.findViewById(R.id.detailTransaksi);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportTransactionDetailActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onStart() {

        db.child("altrump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataDevices = new ArrayList<>();
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.setTimeInMillis(date);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                countDay = 0;
                total = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Device data = postSnapshot.getValue(Device.class);
                    try {
                        Date date = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(date);
                        String val = data.getPrice();
                        val = val.replace(",", "");
                        int pay = Integer.parseInt(val);

                        if (dateChoosen.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && dateChoosen.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                            if (dateChoosen.get(Calendar.DAY_OF_YEAR) == myCal.get(Calendar.DAY_OF_YEAR)) {
                                countDay++;
                                total += (pay * 500);
                            }
                        }
                    } catch (Exception ignored) {

                    }
                }


                String day = Integer.toString(countDay);
                String price = Integer.toString(total);
                tv_income.setText("Rp. " + price);
                tv_transaction.setText(day);
                Log.d(TAG, "onDataChange: total " + total);
                Log.d(TAG, "onDataChange: countDay " + countDay);
                Log.d(TAG, "onDataChange: date " + date);
                Log.d(TAG, "onDataChange: dateChoosen " + dateChoosen.getTimeInMillis());
                Log.d(TAG, "onDataChange: day " + day);
                Log.d(TAG, "onDataChange: price " + price);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//TODO: data per hari belum berubah
        super.onStart();
    }
}
