package com.pens.afdolash.altrump.report.machine;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Machine;

import java.util.List;

public class MachineList extends ArrayAdapter<Machine> {

    private Activity context;
    List<Machine> machines;

    public MachineList(Activity context, List<Machine> machines) {
        super(context, R.layout.layout_machine_detail_laporan_list, machines);
        this.context = context;
        this.machines = machines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_machine_detail_laporan_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.tv_namaSPBU);

        Machine machine = machines.get(position);
        String nama = "SPBU " + machine.getAlamat();
        textViewName.setText(nama);

        return listViewItem;
    }

}
