package com.pens.afdolash.altrump.information.machine;


import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Machine;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MachineFragment extends Fragment {

    ListView listViewMachine;
    List<Machine> machines;

    // Get a reference to your user
    DatabaseReference databaseMachine;

    public MachineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_machine, container, false);

        databaseMachine = FirebaseDatabase.getInstance().getReference("machines");

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
                final MachineList machineAdapter = new MachineList(getActivity(), machines);
                //attaching adapter to the listview

                listViewMachine.setAdapter(machineAdapter);

                listViewMachine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), MachineDetailActivity.class);
                        Machine machine = machines.get(position);
                        intent.putExtra("machine", machine);
                        startActivity(intent);
                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getContext(), MachineAddActivity.class);
                startActivity(intent);

            }
        });

        /*TODO:
        * tampilkan machine detail
        * tampilkan machine form untuk update
        * buat fungsi hapus
        * */

        return view;
    }


}
