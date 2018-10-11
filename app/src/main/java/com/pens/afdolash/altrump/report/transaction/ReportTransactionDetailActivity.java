package com.pens.afdolash.altrump.report.transaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.Position;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pens.afdolash.altrump.ImageHelper;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.DataDevice;
import com.pens.afdolash.altrump.model.Device;
import com.pens.afdolash.altrump.model.Item;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.profile.EditProfileActivity;
import com.pens.afdolash.altrump.profile.ProfileActivity;
import com.pens.afdolash.altrump.splash.SignInActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportTransactionDetailActivity extends AppCompatActivity {

    private static final String TAG = "ReportDetail";
    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;
    int countDays = 0;
    int countMonths = 0;
    int totalMonths = 0;
    String user_key, alamatSPBU;
    String machineID;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<Integer> countTrc;
    RecyclerView rv_listSPBU;
    Machine machine;
    TextView tv_tanggal, tv_month, tv_income, tv_transaction;
    String jumlah, income, tglbaru, thnbaru, day, price;

    int tgl, bulan, tahun;
    int totalElements;
    DatabaseReference db;
    public List<Integer> dataTransaksi;
    public String[] month;

    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DetailAdapter mAdapter;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_transaction_detail);

        month = getResources().getStringArray(R.array.month_list);

        tv_month = findViewById(R.id.tv_month);
        tv_tanggal = findViewById(R.id.tv_tanggal);
        tv_income = findViewById(R.id.tv_income);
        tv_transaction = findViewById(R.id.tv_transaction);

        tgl = (int) getIntent().getExtras().get("tgl");
        bulan = (int) getIntent().getExtras().get("bulan");
        tahun = (int) getIntent().getExtras().get("tahun");
        jumlah = (String) getIntent().getExtras().get("jumlah");
        income = (String) getIntent().getExtras().get("income");
        countTrc = (ArrayList<Integer>) getIntent().getExtras().get("array");
        dataTransaksi = new ArrayList<>();


        recyclerView = findViewById(R.id.rv_listSpbu);
        mAdapter = new DetailAdapter(itemList);

        tglbaru = Integer.toString(tgl);
        thnbaru = Integer.toString(tahun);

        tv_tanggal.setText(tglbaru);
        tv_month.setText(month[bulan]);

        if (jumlah == null || income == null || countTrc == null) {
            tv_transaction.setText("0");
            tv_income.setText("Rp. 0");
            totalElements = 0;
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgBlur = (ImageView) findViewById(R.id.img_blur);
        view = findViewById(R.id.container);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getAuth();

        imgBlur.post(new Runnable() {
            @Override
            public void run() {

                blurBitmap = createBlurBitmap();
                imgBlur.setImageBitmap(blurBitmap);
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

    public void getData(String machineID, final String alamat) {

        db.child(machineID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.set(tahun, bulan, tgl);
                dateChoosen = setTimming(dateChoosen);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                int countTransaksiTotal = 0;
                int countTransaskiBerhasil = 0;
                int countTransaskiGagal = 0;
                int countPengisianBaru = 0;
                int countPengisanUlang = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Device data = postSnapshot.getValue(Device.class);

                    try {
                        Date dates = formatter.parse(data.getDate());
                        Calendar myCal = Calendar.getInstance();
                        myCal.setTime(dates);
                        String val = data.getPrice();
                        val = val.replace(",", "");
                        int pay = Integer.parseInt(val);

                        if (dateChoosen.get(Calendar.MONTH) == myCal.get(Calendar.MONTH) && dateChoosen.get(Calendar.YEAR) == myCal.get(Calendar.YEAR)) {
                            if (dateChoosen.get(Calendar.DAY_OF_YEAR) == myCal.get(Calendar.DAY_OF_YEAR)) {
                                if (data.getStatusB().equals("1")) {
                                    countPengisianBaru++;
                                }
                                if (data.getStatusB().equals("2")) {
                                    countPengisanUlang++;
                                }
                                if (data.getStatusA().equals("1")){
                                    countDays++;
                                    totalMonths += (pay * 500);
                                    countTransaskiBerhasil++;
                                    countTransaksiTotal++;
                                }
                                if (data.getStatusA().equals("2")){
                                    countTransaskiGagal++;
                                    countTransaksiTotal++;
                                }
                            }
                            countMonths++;
                        }

                    } catch (Exception ignored) {

                    }
                }

                item = new Item(alamat, Integer.toString(countTransaksiTotal), Integer.toString(countTransaskiBerhasil),
                        Integer.toString(countTransaskiGagal), Integer.toString(countPengisianBaru), Integer.toString(countPengisanUlang));
                itemList.add(item);
                dataTransaksi.add(countDays);
                Log.d(TAG, "onDataChange: dataTransaksi " + dataTransaksi);

                String day = Integer.toString(countDays);
                String months = Integer.toString(countMonths);
                String price = Integer.toString(totalMonths);

                tv_transaction.setText(day);
                tv_income.setText("Rp. " + price);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAuth() {
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ReportTransactionDetailActivity.this, SignInActivity.class));
                } else {
                    final String email = user.getEmail();
                    db = FirebaseDatabase.getInstance().getReference();

                    db.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Users user = postSnapshot.getValue(Users.class);
                                if (email.equals(user.getEmail())) {
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

    public void getMachine() {

        db.child("machine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();

                countDays = 0;
                countMonths = 0;
                totalMonths = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    machine = postSnapshot.getValue(Machine.class);

                    try {
                        if (machine != null && user_key.equals(machine.getUser_key())) {
                            //adding machine to the list
                            machineID = machine.getId_mesin();
                            alamatSPBU = machine.getAlamat();
                            getData(machineID, alamatSPBU);
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    public void setGrafik(List<Integer> dataTransaksi){
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        if (dataTransaksi!=null){
            for (int i = 0; i < dataTransaksi.size(); i++){
                data.add(new ValueDataEntry(String.valueOf(i), dataTransaksi.get(i)));
            }
        }
        Log.d(TAG, "setGrafik: dataTransaksi" + dataTransaksi);


        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X} " + month[bulan] + " " + thnbaru)
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(0d)
                .format("{%Value} transaksi");

        cartesian.title(false);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.credits().enabled(false);
        cartesian.animation(true);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yScale().minimum(0d);
        cartesian.xAxis(0).title(month[bulan] + " " + thnbaru);
        cartesian.yAxis(0).title("Jumlah transaksi");
        cartesian.yAxis(0).labels().format("{%Value}");
    }

    public Calendar setTimming(Calendar date) {
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);

        return date;
    }
}
