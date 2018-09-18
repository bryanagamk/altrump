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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
import com.pens.afdolash.altrump.profile.EditProfileActivity;
import com.pens.afdolash.altrump.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class ReportTransactionDetailActivity extends AppCompatActivity {

    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;

    private AnyChartView anyChartView;
    private CardView cardMotor, cardCar;
    private ExpandableLinearLayout expandableMotor, expandableCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_transaction_detail);

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
        data.add(new ValueDataEntry("1", 60));
        data.add(new ValueDataEntry("2", 80));
        data.add(new ValueDataEntry("3", 56));
        data.add(new ValueDataEntry("4", 20));
        data.add(new ValueDataEntry("5", 78));
        data.add(new ValueDataEntry("6", 56));
        data.add(new ValueDataEntry("7", 34));
        data.add(new ValueDataEntry("8", 10));
        data.add(new ValueDataEntry("9", 46));

//        TODO : Ambil data Transaksi selama sebulan dan dijadikan chart

        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X} Oktober 2018")
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
        cartesian.xAxis(0).title("Oktober 2018");
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
}
