package com.pens.afdolash.altrump.report.transaction;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;

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
    String[] tahun;
    Calendar input;

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
        getData(input.getTimeInMillis());

        spinnerYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                year = Integer.parseInt(tahun[view.getSelectedIndex()]);
                input.set(year, 0, 1);
                input = setTimming(input);
                getData(input.getTimeInMillis());
            }
        });

        return view;
    }

    public void getData(final long date){
        db.child("altrump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataDevices = new ArrayList<>();
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.set(year, 0, 1);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                count = 0;
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

                        if (dateChoosen.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                            count++;
                            total += (pay * 500);
                        }
                    } catch (Exception ignored) {

                    }
                }


                String day = Integer.toString(count);
                String price = Integer.toString(total);
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
