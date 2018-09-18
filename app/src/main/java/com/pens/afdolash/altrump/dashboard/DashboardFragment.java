package com.pens.afdolash.altrump.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Device;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    DatabaseReference database;

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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.child("altrump").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                System.out.println("Size : " + size );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("machine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                System.out.println("Machine : " + size );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void dateToDay(){

    }

}
