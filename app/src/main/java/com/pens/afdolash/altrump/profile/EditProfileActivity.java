package com.pens.afdolash.altrump.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.ImageHelper;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;

    EditText et_nameEdit, et_emailEdit, et_tmpLahirEdit, et_tglLahirEdit, et_telpEdit, et_locationEdit, et_passwdEdit, et_passwdReEdit;
    RelativeLayout simpan;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    FirebaseAuth.AuthStateListener authListener;
    DatabaseReference db;
    FirebaseAuth auth;
    FirebaseUser user;
    Users users;
    String TAG = "njing";
    private String ref = "";
    String emailData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBlur = (ImageView) findViewById(R.id.img_blur);
        view = findViewById(R.id.container);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        Intent intent = getIntent();
        String fname = intent.getStringExtra("fname");
        final String email = intent.getStringExtra("email");
        String tempat = intent.getStringExtra("tempat");
        String tgl = intent.getStringExtra("tgl");
        String nomorHP = intent.getStringExtra("nomorHP");
        String alamat = intent.getStringExtra("alamat");

        et_nameEdit = findViewById(R.id.et_nameEdit);
        et_emailEdit = findViewById(R.id.et_emailEdit);
        et_tmpLahirEdit = findViewById(R.id.et_tmpLahirEdit);
        et_tglLahirEdit = findViewById(R.id.et_tglLahirEdit);
        et_telpEdit = findViewById(R.id.et_telpEdit);
        et_locationEdit = findViewById(R.id.et_locationEdit);
        et_passwdEdit = findViewById(R.id.et_passwdEdit);
        et_passwdReEdit = findViewById(R.id.et_passwdReEdit);
        simpan = findViewById(R.id.simpanEdit);

        et_nameEdit.setText(fname);
        et_emailEdit.setText(email);
        et_tmpLahirEdit.setText(tempat);
        et_tglLahirEdit.setText(tgl);
        et_telpEdit.setText(nomorHP);
        et_locationEdit.setText(alamat);

        imgBlur.post(new Runnable() {
            @Override
            public void run() {
                blurBitmap = createBlurBitmap();
                imgBlur.setImageBitmap(blurBitmap);
            }
        });

        et_tglLahirEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.d(TAG, "onAuthStateChanged: user " + user.getUid());
                } else {
                    emailData = user.getEmail();
                    Log.d(TAG, "onAuthStateChanged: emailData " + emailData);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String namaBaru = et_nameEdit.getText().toString();
                final String emailBaru = et_emailEdit.getText().toString();
                final String tmpLahirBaru = et_tmpLahirEdit.getText().toString();
                final String tglLahirBaru = et_tglLahirEdit.getText().toString();
                final String telpBaru = et_telpEdit.getText().toString();
                final String locBaru = et_locationEdit.getText().toString();

                db = FirebaseDatabase.getInstance().getReference();

                db.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Users comparator = postSnapshot.getValue(Users.class);
                            if (emailData.equals(comparator.getEmail())){
                                ref = String.valueOf(postSnapshot.getKey());
                                Users users = new Users(locBaru, emailBaru, namaBaru, tmpLahirBaru, telpBaru, tglLahirBaru);
                                db.child("users").child(ref).setValue(users);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_tglLahirEdit.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
