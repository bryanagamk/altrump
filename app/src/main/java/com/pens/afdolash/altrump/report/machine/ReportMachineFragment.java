package com.pens.afdolash.altrump.report.machine;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pens.afdolash.altrump.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportMachineFragment extends Fragment {


    public ReportMachineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_machine, container, false);

        RelativeLayout gotoMachineDetail = view.findViewById(R.id.detailMesin);

        gotoMachineDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportMachineDetailActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
