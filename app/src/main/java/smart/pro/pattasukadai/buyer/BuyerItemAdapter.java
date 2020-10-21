package smart.pro.pattasukadai.buyer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import smart.pro.pattasukadai.Mainbean;
import smart.pro.pattasukadai.OnItemClick;
import smart.pro.pattasukadai.R;
import smart.pro.pattasukadai.app.AppConfig;

import java.util.ArrayList;
import java.util.Random;

public class BuyerItemAdapter extends RecyclerView.Adapter<BuyerItemAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Mainbean> mainbeans;
    private OnItemClick onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView buyerCapital;
        TextView buyername, buyerphone;
        ImageView checkView;

        public MyViewHolder(View view) {
            super((view));

            buyerCapital = (TextView) view.findViewById(R.id.buyerCapital);
            buyername = (TextView) view.findViewById(R.id.buyername);
            buyerphone = (TextView) view.findViewById(R.id.buyerphone);
            checkView = (ImageView) view.findViewById(R.id.checkView);

        }
    }

    public BuyerItemAdapter(Context mainActivityUser, ArrayList<Mainbean> mainbeans, BuyerListActivity onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.mainbeans = mainbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Mainbean> myList) {
        this.mainbeans = myList;
        notifyDataSetChanged();
    }

    public BuyerItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buyer_item_list, parent, false);

        return new BuyerItemAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(BuyerItemAdapter.MyViewHolder holder, final int position) {
        Mainbean bean = mainbeans.get(position);
        if (bean.buyername.length() > 0) {
            holder.buyerCapital.setText(String.valueOf(bean.buyername.charAt(0)).toUpperCase());
        } else {
            holder.buyerCapital.setText("B");

        }
        Random rnd = new Random();
        holder.buyerCapital.setBackgroundTintList(ColorStateList.valueOf(
                Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
        holder.buyername.setText(bean.getBuyername());
        holder.buyerphone.setText("Ph: " + bean.getBuyerphone());
        holder.checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemClick(position);
            }
        });
    }

    public int getItemCount() {
        return mainbeans.size();
    }

}
