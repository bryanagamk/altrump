package com.pens.afdolash.altrump.report.machine;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        TextView tv_idMesin = listViewItem.findViewById(R.id.tv_idMesin);
        ImageView imgStatus = listViewItem.findViewById(R.id.img_status);
        TextView tvStatus = listViewItem.findViewById(R.id.tvStatus);
        TextView tv_tglMesin1 = listViewItem.findViewById(R.id.tv_tglMesin1);
        TextView tv_jamHidup = listViewItem.findViewById(R.id.tv_jamHidup);
        TextView tv_tglMesin2 = listViewItem.findViewById(R.id.tv_tglMesin2);
        TextView tv_jamMati = listViewItem.findViewById(R.id.tv_jamMati);

        Machine machine = machines.get(position);
        String nama = "SPBU " + machine.getAlamat();
        String id = machine.getId_mesin();
        String status = machine.getStatus();
        String tgl = machine.getDate();
        String jamHidup = machine.getJamHidup();
        String jamMati = machine.getJamMati();

        textViewName.setText(nama);
        tv_idMesin.setText(id);
        tvStatus.setText(status);
        tv_tglMesin1.setText(tgl);
        tv_tglMesin2.setText(tgl);
        tv_jamHidup.setText(jamHidup);
        tv_jamMati.setText(jamMati);
        if (status.equals("MATI")){
            imgStatus.setImageResource(R.drawable.ic_offline_24dp);
        }

        return listViewItem;
    }

}
