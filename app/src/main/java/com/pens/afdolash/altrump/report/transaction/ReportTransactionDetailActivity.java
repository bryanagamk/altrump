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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.pens.afdolash.altrump.ImageHelper;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Machine;
import com.pens.afdolash.altrump.profile.EditProfileActivity;
import com.pens.afdolash.altrump.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ReportTransactionDetailActivity extends AppCompatActivity {

    private static final String TAG = "ReportDetail";
    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;
    ListView listViewMachine;
    List<Machine> machines;
    ArrayList<Integer> countTrc;
    TextView tv_tanggal, tv_month, tv_income, tv_transaction;
    String jumlah, income, tglbaru, thnbaru;
    int tgl, bulan, tahun;

    private AnyChartView anyChartView;
    private CardView cardMotor, cardCar;
    private ExpandableLinearLayout expandableMotor, expandableCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_transaction_detail);

        final String[] month = getResources().getStringArray(R.array.month_list);

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
        Log.d(TAG, "onCreate: countTrc " + countTrc);


        tglbaru = Integer.toString(tgl);
        thnbaru = Integer.toString(tahun);

        tv_tanggal.setText(tglbaru);
        tv_month.setText(month[bulan]);
        tv_income.setText("Rp. " + income);
        tv_transaction.setText(jumlah);

        int totalElements = countTrc.size();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardMotor = findViewById(R.id.card_motor);
        cardCar = findViewById(R.id.card_car);
        expandableMotor = findViewById(R.id.expandable_motor);
        expandableCar = findViewById(R.id.expandable_car);

        imgBlur = (ImageView) findViewById(R.id.img_blur);
        view = findViewById(R.id.container);

        imgBlur.post(new Runnable() {
            @Override
            public void run() {
                blurBitmap = createBlurBitmap();
                imgBlur.setImageBitmap(blurBitmap);
            }
        });

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        anyChartView.setLicenceKey("");

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (int index = 0; index < totalElements; index++){
            Log.d(TAG, "onCreate: totalEl " + totalElements);
            data.add(new ValueDataEntry(index, countTrc.get(index)));

        }

//        TODO : Ambil data Transaksi selama sebulan dan dijadikan chart

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

        anyChartView.setChart(cartesian);

        cardMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableMotor.toggle();
            }
        });

        cardCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableCar.toggle();
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

    public void getData(final long date) {

    }
}
