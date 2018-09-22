package com.pens.afdolash.altrump.information.machine;

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
        super(context, R.layout.layout_machine_list, machines);
        this.context = context;
        this.machines = machines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_machine_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.tv_namaSPBU);
        TextView textViewIdMesin = listViewItem.findViewById(R.id.tv_idMesin);
        ImageView imageView = listViewItem.findViewById(R.id.img_status);

        Machine machine = machines.get(position);
        String nama = "SPBU " + machine.getAlamat();
        textViewName.setText(nama);
        imageView.setImageResource(R.drawable.ic_offline_24dp);
        textViewIdMesin.setText(machine.getId_mesin());

        return listViewItem;
    }

}
