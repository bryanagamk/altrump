package com.pens.afdolash.altrump.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    ListView listViewMachine;

    List<Machine> machines;

    // Get a reference to your user
    DatabaseReference databaseMachine;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TextView countTransaction = view.findViewById(R.id.countTransaction);
        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_income = view.findViewById(R.id.tv_income);
        TextView tv_transaction = view.findViewById(R.id.tv_transaction);
        TextView tv_monthdashboard = view.findViewById(R.id.tv_monthDashboard);

        //Tampilkan daftar mesin
        databaseMachine = FirebaseDatabase.getInstance().getReference("machine");

        listViewMachine = view.findViewById(R.id.listViewMachine);

        machines = new ArrayList<>();

        databaseMachine.addValueEventListener(new ValueEventListener() {
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
                MachineList machineAdapter = new MachineList(getActivity(), machines);
                //attaching adapter to the listview
                listViewMachine.setAdapter(machineAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Cari Tanggal
        String date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        String month = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_monthdashboard.setText(month);

        //Cari Total Transaksi
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.child("altrump").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
//                System.out.println("Size : " + size );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("machine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
//                System.out.println("Machine : " + size );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
