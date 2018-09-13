package com.pens.afdolash.altrump.profile;

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

import com.pens.afdolash.altrump.ImageHelper;
import com.pens.afdolash.altrump.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgBlur;
    private View view;
    private Bitmap blurBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit :
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                break;
        }

        return true;
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
