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

    List<Machine> machines;
    MachineList machineAdapter;
    // Get a reference to your user
    DatabaseReference db;
    int countDay;
    int countMonth;
    int total = 0;
    int price;

    List<DataDevice> dataDevices;

    Date today = new Date();

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
                dataDevices = new ArrayList<>();
                Calendar now = Calendar.getInstance();
                countDay = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Device data = postSnapshot.getValue(Device.class);
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                        Date date = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(date);

                        if (now.equals(myCal))
                            countDay++;

                        if (now.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && now.get(Calendar.YEAR) == myCal.get(Calendar.YEAR))
                            countMonth++;

                        DataDevice dataDevice = new DataDevice(myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.YEAR), new ArrayList<Device>());

                        if (dataDevices.contains(dataDevice)){
                            dataDevices.get(dataDevices.indexOf(dataDevice)).getDevices().add(data);
                        } else {
                            dataDevice.getDevices().add(data);
                            dataDevices.add(dataDevice);
                        }
                    } catch (Exception ignored){

                    }
                }

                for (DataDevice dataDevice : dataDevices){
                    if (dataDevice.getMonth() == (now.get(Calendar.MONTH) + 1) && dataDevice.getYears() == now.get(Calendar.YEAR)){
                        for (Device device : dataDevice.getDevices()){
                            String val = device.getPrice();
                            val.replace(",","");
                            try {
                                int pay = Integer.parseInt(val);
                                total += (pay*500);
                            } catch (Exception e){

                            }
                        }
                    }
                    Log.d(TAG, "onDataChange: bulan " + dataDevice.getDevices().size());
                }

                String day = Integer.toString(countDay);
                String month = Integer.toString(countMonth);
                String price = Integer.toString(total);
                tv_income.setText(price);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        countTransaction = view.findViewById(R.id.countTransaction);
        TextView tv_date = view.findViewById(R.id.tv_date);
        tv_income = view.findViewById(R.id.tv_income);
        tv_transaction = view.findViewById(R.id.tv_transaction);
        TextView tv_monthdashboard = view.findViewById(R.id.tv_monthDashboard);

        //Tampilkan daftar mesin
        db = FirebaseDatabase.getInstance().getReference();
        db.keepSynced(true);


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
