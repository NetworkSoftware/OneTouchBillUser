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
    boolean includeGst;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quantity, total, perquantity, cgst, igst, sgst, particulars;
        ImageView delete;

        public MyViewHolder(View view) {
            super((view));

            quantity = (TextView) view.findViewById(R.id.particularsquantity);
            total = (TextView) view.findViewById(R.id.particularstotal);
            perquantity = (TextView) view.findViewById(R.id.particularsperquantity);
            cgst = (TextView) view.findViewById(R.id.particularscgst);
            igst = (TextView) view.findViewById(R.id.particularsigst);
            sgst = (TextView) view.findViewById(R.id.particularssgst);
            particulars = (TextView) view.findViewById(R.id.particulars);
            delete = (ImageView) view.findViewById(R.id.delete);

        }
    }

    public ParticularItemAdapter(Context mainActivityUser, ArrayList<Particularbean> particularbeans,
                                 OnItemClick onItemClick, boolean includeGst) {
        this.mainActivityUser = mainActivityUser;
        this.particularbeans = particularbeans;
        this.onItemClick = onItemClick;

        this.includeGst = includeGst;
    }

    public void notifyData(boolean includeGst) {
        this.includeGst = includeGst;
        notifyDataSetChanged();
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
        holder.particulars.setText(bean.particular.replace(" inch","\""));

        if (position % 2 == 0) {
            holder.quantity.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.sgst.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.igst.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.perquantity.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.cgst.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));
            holder.total.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.white)));

        } else {
            holder.quantity.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.sgst.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.igst.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.perquantity.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.cgst.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
            holder.total.setBackgroundTintList(ColorStateList.valueOf(mainActivityUser.getResources().getColor(
                    R.color.grayLight)));
        }
        holder.quantity.setText(bean.quantity);
        holder.perquantity.setText(bean.perquantity);
        float quan = Float.parseFloat(bean.quantity);
        float perQuanPri = Float.parseFloat(bean.perquantity);
        float total = quan * perQuanPri;
        if (includeGst) {
            float cgstVal = ((bean.cgst.length() > 0 ? Float.parseFloat(bean.cgst) : 0) * total) / 100f;
            float igstVal = ((bean.igst.length() > 0 ? Float.parseFloat(bean.igst) : 0) * total) / 100f;
            float sgstVal = ((bean.sgst.length() > 0 ? Float.parseFloat(bean.sgst) : 0) * total) / 100f;

            holder.cgst.setText(String.valueOf(cgstVal));
            holder.igst.setText(String.valueOf(igstVal));
            holder.sgst.setText(String.valueOf(sgstVal));

            holder.total.setText(String.valueOf(total + igstVal + sgstVal + cgstVal));
            holder.igst.setVisibility(View.VISIBLE);
            holder.sgst.setVisibility(View.VISIBLE);
            holder.cgst.setVisibility(View.VISIBLE);
        } else {
            holder.total.setText(String.valueOf(total));
            holder.igst.setVisibility(View.GONE);
            holder.sgst.setVisibility(View.GONE);
            holder.cgst.setVisibility(View.GONE);
        }

        holder.particulars.setOnClickListener(new View.OnClickListener() {
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
