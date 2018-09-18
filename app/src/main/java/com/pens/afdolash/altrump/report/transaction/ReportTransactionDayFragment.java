package com.pens.afdolash.altrump.report.transaction;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pens.afdolash.altrump.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportTransactionDayFragment extends Fragment {


    public ReportTransactionDayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_transaction_day, container, false);

        RelativeLayout relativeLayout = view.findViewById(R.id.detailTransaksi);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportTransactionDetailActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

    /*TODO: Estimasi pemasukan untuk harian*/

}
