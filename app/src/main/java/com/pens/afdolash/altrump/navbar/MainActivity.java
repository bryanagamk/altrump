package com.pens.afdolash.altrump.navbar;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.pens.afdolash.altrump.dashboard.DashboardFragment;
import com.pens.afdolash.altrump.information.InformationFragment;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.report.ReportFragment;
import com.pens.afdolash.altrump.settings.SettingsFragment;
import com.pens.afdolash.altrump.navbar.menu.DrawerAdapter;
import com.pens.afdolash.altrump.navbar.menu.DrawerItem;
import com.pens.afdolash.altrump.navbar.menu.SimpleItem;
import com.pens.afdolash.altrump.navbar.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int NAV_DASHBOARD = 0;
    private static final int NAV_INFORMATION = 1;
    private static final int NAV_REPORT = 2;
    private static final int NAV_SETTINGS = 3;
    private static final int NAV_LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_navigation_left)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(NAV_DASHBOARD).setChecked(true),
                createItemFor(NAV_INFORMATION),
                createItemFor(NAV_REPORT),
                createItemFor(NAV_SETTINGS),
                new SpaceItem(48),
                createItemFor(NAV_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.rc_menu);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(NAV_DASHBOARD);
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case NAV_DASHBOARD :
                slidingRootNav.closeMenu();
                showFragment(new DashboardFragment());
                setToolbarTitle("Dashboard");
                setAppBarElevation(true);
                break;
            case NAV_INFORMATION :
                slidingRootNav.closeMenu();
                showFragment(new InformationFragment());
                setToolbarTitle("Informasi");
                setAppBarElevation(false);
                break;
            case NAV_REPORT :
                slidingRootNav.closeMenu();
                showFragment(new ReportFragment());
                setToolbarTitle("Laporan");
                setAppBarElevation(false);
                break;
            case NAV_SETTINGS :
                slidingRootNav.closeMenu();
                showFragment(new SettingsFragment());
                setToolbarTitle("Pengaturan");
                setAppBarElevation(true);
                break;
            case NAV_LOGOUT :
                finish();
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    private void setToolbarTitle(String s) {
        getSupportActionBar().setTitle(s);
    }

    private void setAppBarElevation(boolean b) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (b)
                appBarLayout.setElevation(8);
            else
                appBarLayout.setElevation(0);
        }
    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.colorIcon))
                .withTextTint(color(R.color.colorTitle))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.menu_drawer_titles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.menu_drawer_icons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}
