package smart.pro.pattasukadai.invoice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import smart.pro.pattasukadai.OnItemClick;
import smart.pro.pattasukadai.R;

public class ParticularItemAdapter extends RecyclerView.Adapter<ParticularItemAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Particularbean> particularbeans;
    private OnItemClick onItemClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView no, totalCal, total, name;
        ImageView delete,edit;

        public MyViewHolder(View view) {
            super((view));

            no = view.findViewById(R.id.no);
            totalCal = view.findViewById(R.id.totalCal);
            total = view.findViewById(R.id.total);
            name = view.findViewById(R.id.name);
            delete = view.findViewById(R.id.delete);
            edit = view.findViewById(R.id.edit);

        }
    }

    public ParticularItemAdapter(Context mainActivityUser, ArrayList<Particularbean> particularbeans, OnItemClick onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.particularbeans = particularbeans;
        this.onItemClick = onItemClick;
    }

    public void notifyData(ArrayList<Particularbean> particularbeans) {
        this.particularbeans = particularbeans;
        notifyDataSetChanged();
    }

    public ParticularItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.particular_item_list, parent, false);

        return new ParticularItemAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ParticularItemAdapter.MyViewHolder holder, final int position) {
        Particularbean bean = particularbeans.get(position);
        holder.name.setText(bean.particular.replace(" inch", "\""));

        if (position % 2 == 0) {
            holder.name.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.totalCal.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.total.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));

        } else {
            holder.name.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.totalCal.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.total.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
        }
        holder.no.setText(bean.id);
        String quanS = "1";
        if (bean.getQuantity() != null && bean.getQuantity().length() > 0) {
            quanS = bean.getQuantity();
        }
        int quan = Integer.parseInt(quanS);
        float perQuanPri = Float.parseFloat(bean.perquantity);
        float total = quan * perQuanPri;
        holder.total.setText(total + "");
        holder.totalCal.setText(quan + "*" + perQuanPri);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemEditClick(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemDeleteClick(position);
            }
        });
    }

    public int getItemCount() {
        return particularbeans.size();
    }

}
