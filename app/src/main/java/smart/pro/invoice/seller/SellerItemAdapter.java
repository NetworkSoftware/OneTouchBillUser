package smart.pro.invoice.seller;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Random;

import smart.pro.invoice.Mainbean;
import smart.pro.invoice.OnItemClick;
import smart.pro.invoice.R;

public class SellerItemAdapter extends RecyclerView.Adapter<SellerItemAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Mainbean> mainbeans;
    private OnItemClick onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView buyerCapital, sellername, selleraddress, sellerphone, sellergstNo,sellerbankName,selleraccountNo,sellerifcNo;
        ImageView checkView;

        public MyViewHolder(View view) {
            super((view));

            buyerCapital = (TextView) view.findViewById(R.id.buyerCapital);
            sellername = (TextView) view.findViewById(R.id.sellername);
            selleraddress = (TextView) view.findViewById(R.id.selleraddress);
            sellerphone = (TextView) view.findViewById(R.id.sellerphone);
            sellergstNo = (TextView) view.findViewById(R.id.sellergstNo);
            sellerbankName = (TextView) view.findViewById(R.id.sellerbankName);
            selleraccountNo = (TextView) view.findViewById(R.id.selleraccountNo);
            sellerifcNo = (TextView) view.findViewById(R.id.sellerifcNo);

            checkView = (ImageView) view.findViewById(R.id.checkView);

        }
    }

    public SellerItemAdapter(Context mainActivityUser, ArrayList<Mainbean> mainbeans, SellerListActivity onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.mainbeans = mainbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Mainbean> myList) {
        this.mainbeans = myList;
        notifyDataSetChanged();
    }

    public SellerItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_item_list, parent, false);

        return new SellerItemAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SellerItemAdapter.MyViewHolder holder, final int position) {
        Mainbean bean = mainbeans.get(position);
        if (bean.sellername.length()>0) {
            holder.buyerCapital.setText(String.valueOf(bean.sellername.charAt(0)).toUpperCase());
        }else{
            holder.buyerCapital.setText("B");

        }
        Random rnd = new Random();
        holder.buyerCapital.setBackgroundTintList(ColorStateList.valueOf(
                Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
        holder.sellername.setText(bean.getSellername());
        holder.selleraddress.setText(bean.getSelleraddress());
        holder.sellergstNo.setText("GST: " + bean.getSellergstNo());
        holder.sellerphone.setText("Ph: " + bean.getSellerphone());
        holder.sellerbankName.setText("Bank Name: " + bean.getBankname());
        holder.selleraccountNo.setText("Account No: " + bean.getAccountNo());
        holder.sellerifcNo.setText("IFSC No: " + bean.getIfcno());


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
