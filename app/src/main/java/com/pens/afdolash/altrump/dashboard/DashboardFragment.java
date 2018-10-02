package com.pens.afdolash.altrump.dashboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    ListView listViewMachine;
    String TAG = "Mainnjing";

    List<Machine> machines;
    MachineList machineAdapter;
    // Get a reference to your user
    DatabaseReference db;
    int countDay;
    int countMonth;
    int totalMonth = 0;
    String day;
    String months;
    String price;

    List<DataDevice> dataDevices;

    TextView tv_transaction, tv_income, countTransaction;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        machineAdapter = new MachineList(getContext(), machines);
        listViewMachine.setAdapter(machineAdapter);

        db.child("machine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                machines.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Machine machine = postSnapshot.getValue(Machine.class);
                    machines.add(machine);
                }

                machineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db.child("altrump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataDevices = new ArrayList<>();
                Calendar now = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                countDay = 0;
                countMonth = 0;
                totalMonth = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Device data = postSnapshot.getValue(Device.class);

                    try {
                        Date date = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(date);
                        String val = data.getPrice();
                        val = val.replace(",", "");
                        int pay = Integer.parseInt(val);

                        if (now.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && now.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                            if (now.get(Calendar.DAY_OF_YEAR) == myCal.get(Calendar.DAY_OF_YEAR)) {
                                countDay++;
                            }
                            totalMonth += (pay * 500);
                            countMonth++;
                        }
                    } catch (Exception ignored) {

                    }
                }

                String day = Integer.toString(countDay);
                String month = Integer.toString(countMonth);
                String price = Integer.toString(totalMonth);
                tv_income.setText("Rp. " + price);
                countTransaction.setText(day);
                tv_transaction.setText(month);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        countTransaction = view.findViewById(R.id.countTransaction);
        TextView tv_date = view.findViewById(R.id.tv_date);
        tv_income = view.findViewById(R.id.tv_income);
        tv_transaction = view.findViewById(R.id.tv_transaction);
        TextView tv_monthdashboard = view.findViewById(R.id.tv_monthDashboard);

        db = FirebaseDatabase.getInstance().getReference();

        listViewMachine = view.findViewById(R.id.listViewMachine);
        machines = new ArrayList<>();

        /*try{
            Log.d(TAG, "onCreateView: " + getArguments());
            day = getArguments() != null ? getArguments().getString("day") : null;
            months = getArguments() != null ? getArguments().getString("month") : null;
            price = getArguments() != null ? getArguments().getString("price") : null;
            Log.d(TAG, "onDataChange day 3: " + day);
            Log.d(TAG, "onDataChange month 3: " + months);
            Log.d(TAG, "onDataChange price 3: " + price);

            tv_income.setText("Rp. " + price);
            countTransaction.setText(day);
            tv_transaction.setText(months);
        } catch (Exception e) {
            tv_income.setText("Rp. 0");
            countTransaction.setText("0");
            tv_transaction.setText("0");
        }*/


        // Cari Tanggal
        String date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        String month = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_monthdashboard.setText(month);

        return view;
    }
}
