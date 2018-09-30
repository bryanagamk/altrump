package com.pens.afdolash.altrump.firebaseHelper;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class DatabaseHelper {

    DatabaseReference db;
    List<DataDevice> dataDevices;
    long date;
    int countDay;
    int total;

    TextView tv_transaction, tv_income;

    public void getMonth(){
        db = FirebaseDatabase.getInstance().getReference();
        db.keepSynced(true);
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
    }
}
