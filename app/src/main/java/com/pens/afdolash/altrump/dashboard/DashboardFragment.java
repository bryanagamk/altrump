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
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    ListView listViewMachine;

    List<Machine> machines;
    MachineList machineAdapter;
    // Get a reference to your user
    DatabaseReference db;
    int countDay;
    int countMonth;
    int total = 0;
    int price;
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

                //clearing the previous artist list
                machines.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting machine
                    Machine machine = postSnapshot.getValue(Machine.class);
                    //adding machine to the list
                    machines.add(machine);
                }

                //creating adapter
                machineAdapter.notifyDataSetChanged();
                //attaching adapter to the listview
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db.child("altrump").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postponeSnapshot : postSnapshot.getChildren()){
                        String key = postponeSnapshot.getKey();
                        Date today = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        Date var = null;

                        if (key.equals("date")){
                            String value = postponeSnapshot.getValue().toString();
                            try {
                                var = formatter.parse(value);

                                if (var.getYear() == today.getYear() && var.getMonth() == today.getMonth()){
                                    if (var.getDate() == today.getDate()){
                                        countDay++;
                                    }
                                    countMonth++;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (key.equals("price")){
                            String value = postponeSnapshot.getValue().toString();
                            value = value.replace(",","");
                            try {
                                price = Integer.parseInt(value);
                                price *= 500;
                            } catch (Exception ignored) {

                            }
                            total += price;

                        }
                    }
                }
                String totals = Integer.toString(total);
                String countDays = Integer.toString(countDay);
                String countMonths = Integer.toString(countMonth);
                tv_income.setText("Rp. " + totals);
                countTransaction.setText(countDays);
                tv_transaction.setText(countMonths);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        countTransaction = view.findViewById(R.id.countTransaction);
        TextView tv_date = view.findViewById(R.id.tv_date);
        tv_income = view.findViewById(R.id.tv_income);
        tv_transaction = view.findViewById(R.id.tv_transaction);
        TextView tv_monthdashboard = view.findViewById(R.id.tv_monthDashboard);

        //Tampilkan daftar mesin
        db = FirebaseDatabase.getInstance().getReference();


        listViewMachine = view.findViewById(R.id.listViewMachine);

        machines = new ArrayList<>();


        // Cari Tanggal
        String date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        String month = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_monthdashboard.setText(month);

        return view;
    }

}
