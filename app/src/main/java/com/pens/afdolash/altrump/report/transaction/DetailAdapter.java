package com.pens.afdolash.altrump.report.transaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Item;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaSPBU, tv_jmlPbaruAtas, tv_jmlTBerhasil, tv_jmlTGagal, tv_jmlPbaru, tv_jmlPulang;
        public ExpandableLinearLayout expandableLinearLayout;

        public MyViewHolder(View view) {
            super(view);
            tv_namaSPBU = view.findViewById(R.id.tv_namaSPBU);
            tv_jmlPbaruAtas = view.findViewById(R.id.tv_jmlPbaruAtas);
            tv_jmlTBerhasil = view.findViewById(R.id.tv_jmlTBerhasil);
            tv_jmlTGagal = view.findViewById(R.id.tv_jmlTGagal);
            tv_jmlPbaru = view.findViewById(R.id.tv_jmlPbaru);
            tv_jmlPulang = view.findViewById(R.id.tv_jmlPulang);
            expandableLinearLayout = (ExpandableLinearLayout) view.findViewById(R.id.expandable_motor);

            expandableLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expandableLinearLayout.toggle();
                }
            });
        }
    }


    public DetailAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_spbu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tv_namaSPBU.setText(item.getTv_namaSPBU());
        holder.tv_jmlPbaruAtas.setText(item.getTv_jmlPbaruAtas());
        holder.tv_jmlTBerhasil.setText(item.getTv_jmlTBerhasil());
        holder.tv_jmlTGagal.setText(item.getTv_jmlTGagal());
        holder.tv_jmlPbaru.setText(item.getTv_jmlPbaru());
        holder.tv_jmlPulang.setText(item.getTv_jmlPulang());
        holder.setIsRecyclable(false);
        holder.expandableLinearLayout.setInRecyclerView(true);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
