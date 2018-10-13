package com.pens.afdolash.altrump.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.splash.SignInActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
    int countDays = 0;
    int countMonths = 0;
    int totalMonths = 0;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;
    String user_key;
    String machineID;

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
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    public void getData(String machineID){

        db.child(machineID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataDevices = new ArrayList<>();
                Calendar now = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Device data = postSnapshot.getValue(Device.class);

                    try {
                        Date date = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(date);
                        String val = data.getPrice();
                        val = val.replace(",", "");
                        int pay = Integer.parseInt(val);

                        if (data.getStatusA().equals("1")){
                            if (now.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && now.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                                if (now.get(Calendar.DAY_OF_YEAR) == myCal.get(Calendar.DAY_OF_YEAR)) {
                                    countDays++;
                                }
                                totalMonths += (pay * 500);
                                countMonths++;
                            }
                        }

                        String day = Integer.toString(countDays);
                        String months = Integer.toString(countMonths);
                        String price = Integer.toString(totalMonths);
                        tv_income.setText("Rp. " + price);
                        countTransaction.setText(day);
                        tv_transaction.setText(months);

                    } catch (Exception ignored) {

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAuth(){
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String email = user.getEmail();
                    db = FirebaseDatabase.getInstance().getReference();

                    db.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Users user = postSnapshot.getValue(Users.class);
                                if (email.equals(user.getEmail())){
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
        if(getActivity() != null){
            machineAdapter = new MachineList(getContext(), machines);
            listViewMachine.setAdapter(machineAdapter);
        }


        db.child("machine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                machines.clear();

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
                            machines.add(machine);
                        }
                    } catch (Exception e){

                    }
                }

                if (getActivity() != null){
                    machineAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        // Cari Tanggal
        String date = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date);

        String month = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        tv_monthdashboard.setText(month);

        getAuth();
        return view;
    }
}
