package com.pens.afdolash.altrump.report.transaction;


import android.content.Intent;
import android.os.Build;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;

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

        tv_transaction = view.findViewById(R.id.tv_transaction);
        tv_income = view.findViewById(R.id.tv_income);

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
        getData(date);

        cv_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull final CalendarView calendarView, int i, int i1, int i2) {
                input = Calendar.getInstance();
                input.set(i, i1, i2);
                input = setTimming(input);
                getData(input.getTimeInMillis());
                Log.d(TAG, "onSelectedDayChange: " + countTrc);
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

    public void getData(final long date) {

        db.child("altrump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataDevices = new ArrayList<>();
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.setTimeInMillis(date);
                dateChoosen = setTimming(dateChoosen);

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                countDay = 0;
                i = 0;
                total = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Device data = postSnapshot.getValue(Device.class);
                    try {
                        Date date1 = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(date1);
                        String val = data.getPrice();
                        val = val.replace(",", "");
                        int pay = Integer.parseInt(val);

                        if (dateChoosen.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && dateChoosen.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                            if (dateChoosen.get(Calendar.DAY_OF_YEAR) == myCal.get(Calendar.DAY_OF_YEAR)) {
                                countDay++;
                                total += (pay * 500);
                            }
                            countTrc = new ArrayList<>();
                            countTrc.set(i, countDay);
                            i++;
                        }
                    } catch (Exception ignored) {

                    }
                }
                Log.d(TAG, "onDataChange: countTrc " + countTrc);
                Log.d(TAG, "onDataChange: countTrc size " + countTrc.size());
                day = Integer.toString(countDay);
                price = Integer.toString(total);
                tv_income.setText("Rp. " + price);
                tv_transaction.setText(day);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
