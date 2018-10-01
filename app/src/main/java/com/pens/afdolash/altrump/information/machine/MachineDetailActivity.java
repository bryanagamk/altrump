package com.pens.afdolash.altrump.information.machine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Machine;

public class MachineDetailActivity extends AppCompatActivity {

    TextView tv_namaMesin, tv_idMesin;
    DatabaseReference mDatabase;
    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_namaMesin = findViewById(R.id.tv_namaMesin);
        tv_idMesin = findViewById(R.id.tv_idMesin);

        final Machine machine = (Machine) getIntent().getExtras().get("machine");
        mDatabase = FirebaseDatabase.getInstance().getReference("machines");

        tv_namaMesin.setText(machine.getAlamat());
        tv_idMesin.setText(machine.getId_mesin());

        RelativeLayout bt_delete = findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: idChild " + machine.getId_mesin());
                mDatabase.getRef().child(machine.getId_mesin()).removeValue();
                finish();
            }
        });

        RelativeLayout bt_update = findViewById(R.id.bt_update);
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachineDetailActivity.this, MachineUpdateActivity.class);
                intent.putExtra("machine", machine);
                startActivity(intent);
            }
        });

    }
}
