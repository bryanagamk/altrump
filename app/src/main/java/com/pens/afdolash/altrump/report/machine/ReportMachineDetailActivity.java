package com.pens.afdolash.altrump.report.machine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.ImageHelper;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.dashboard.MachineList;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.profile.EditProfileActivity;
import com.pens.afdolash.altrump.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ReportMachineDetailActivity extends AppCompatActivity {

    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;

    ListView listViewMachine;
    List<Machine> machines;
    // Get a reference to your user
    DatabaseReference databaseMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_machine_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBlur = (ImageView) findViewById(R.id.img_blur);
        view = findViewById(R.id.container);

        imgBlur.post(new Runnable() {
            @Override
            public void run() {
                blurBitmap = createBlurBitmap();
                imgBlur.setImageBitmap(blurBitmap);
            }
        });

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
                com.pens.afdolash.altrump.dashboard.MachineList machineAdapter = new MachineList(ReportMachineDetailActivity.this, machines);
                //attaching adapter to the listview
                listViewMachine.setAdapter(machineAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public Bitmap createBlurBitmap() {
        Bitmap bitmap = captureView(view);
        if (bitmap != null) {
            ImageHelper.blurBitmapWithRenderscript(
                    RenderScript.create(this),
                    bitmap);
        }

        return bitmap;
    }

    public Bitmap captureView(View view) {
        // Create a Bitmap with the same dimensions as the View
        Bitmap image = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality

        // Draw the view inside the Bitmap
        Canvas canvas = new Canvas(image);
        view.draw(canvas);

        // Make it frosty
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        // ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000); // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(image, 0, 0, paint);

        return image;
    }
}
