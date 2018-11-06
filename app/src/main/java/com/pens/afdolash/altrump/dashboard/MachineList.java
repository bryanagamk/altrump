package com.pens.afdolash.altrump.dashboard;

import android.app.Activity;
import android.content.Context;
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

    private Context context;
    List<Machine> machines;

    public MachineList(Context context, List<Machine> machines) {
        super(context, R.layout.layout_machine_dashboard_list, machines);
        this.context = context;
        this.machines = machines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewItem = inflater.inflate(R.layout.layout_machine_dashboard_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.tv_namaSPBU);
        TextView textViewJamHidup = listViewItem.findViewById(R.id.tv_jamHidup);
        TextView textViewJamMati = listViewItem.findViewById(R.id.tv_jamMati);
        TextView textViewStatus = listViewItem.findViewById(R.id.tvStatus);
        ImageView imgStatus = listViewItem.findViewById(R.id.imgStatus);

        Machine machine = machines.get(position);
        String nama = "SPBU " + machine.getAlamat();
        String jamMati = machine.getJamMati();
        String jamHidup = machine.getJamHidup();
        String status = machine.getStatus();

        textViewName.setText(nama);
        textViewJamHidup.setText(jamHidup);
        textViewJamMati.setText(jamMati);
        textViewStatus.setText(status);
        if (status.equals("HIDUP")){
            imgStatus.setImageResource(R.drawable.ic_online_24dp);
        }else {
            imgStatus.setImageResource(R.drawable.ic_offline_24dp);
        }

        return listViewItem;
    }

}
