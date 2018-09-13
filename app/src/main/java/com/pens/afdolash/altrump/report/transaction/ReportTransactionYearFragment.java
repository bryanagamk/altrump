package com.pens.afdolash.altrump.report.transaction;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pens.afdolash.altrump.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportTransactionYearFragment extends Fragment {

    private MaterialSpinner spinnerYear;

    public ReportTransactionYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_transaction_year, container, false);

        spinnerYear = (MaterialSpinner) view.findViewById(R.id.spinner_year);
        spinnerYear.setItems(getResources().getStringArray(R.array.year_list));
        spinnerYear.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat_regular), Typeface.NORMAL);

        return view;
    }

}
