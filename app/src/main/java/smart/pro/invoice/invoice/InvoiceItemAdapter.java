package smart.pro.invoice.invoice;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import smart.pro.invoice.MainEditActivity;
import smart.pro.invoice.Mainbean;
import smart.pro.invoice.OnItemClick;
import smart.pro.invoice.R;
import smart.pro.invoice.app.AppConfig;

import java.util.ArrayList;
import java.util.Random;

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.MyViewHolder> implements Filterable {

    private Context mainActivityUser;
    private ArrayList<Mainbean> mainbeans;
    private ArrayList<Mainbean> mainbeansFiltered;
    private OnItemClick onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView buyerCapital, sellerbillNo, invoice_date, sellername, selleraddress, sellerphone, sellergstNo;
        TextView quantity, total;
        TextView buyername, buyeraddress, buyerphone, buyergstNo;
        ImageView shareView, deleteview, editview;

        public MyViewHolder(View view) {
            super((view));

            buyerCapital = (TextView) view.findViewById(R.id.buyerCapital);
            sellerbillNo = (TextView) view.findViewById(R.id.sellerbillNo);
            invoice_date = (TextView) view.findViewById(R.id.invoice_date);
            sellername = (TextView) view.findViewById(R.id.sellername);
            selleraddress = (TextView) view.findViewById(R.id.selleraddress);
            sellerphone = (TextView) view.findViewById(R.id.sellerphone);
            sellergstNo = (TextView) view.findViewById(R.id.sellergstNo);
            quantity = (TextView) view.findViewById(R.id.quantity);
            total = (TextView) view.findViewById(R.id.total);
            buyername = (TextView) view.findViewById(R.id.buyername);
            buyeraddress = (TextView) view.findViewById(R.id.buyeraddress);
            buyerphone = (TextView) view.findViewById(R.id.buyerphone);
            buyergstNo = (TextView) view.findViewById(R.id.buyergstNo);
            shareView = (ImageView) view.findViewById(R.id.shareView);
            deleteview = (ImageView) view.findViewById(R.id.deleteview);
            editview = (ImageView) view.findViewById(R.id.editview);

        }
    }

    public InvoiceItemAdapter(Context mainActivityUser, ArrayList<Mainbean> mainbeans, InvoiceListActivity onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.mainbeans = mainbeans;
        this.mainbeansFiltered = mainbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Mainbean> myList) {
        this.mainbeansFiltered = myList;
        notifyDataSetChanged();
    }

    public InvoiceItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_list_item, parent, false);

        return new InvoiceItemAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(InvoiceItemAdapter.MyViewHolder holder, final int position) {
        Mainbean bean = mainbeansFiltered.get(position);
        if (bean.buyername.length() > 0) {
            holder.buyerCapital.setText(String.valueOf(bean.buyername.charAt(0)).toUpperCase());
        } else {
            holder.buyerCapital.setText("B");

        }
        try {
            holder.sellerbillNo.setText(AppConfig.intToString(Integer.parseInt(bean.getSellerbillNo()), 5));
        } catch (Exception e) {
            Log.e("xxxxxxxxxxxx", e.toString());
        }
        Random rnd = new Random();
        holder.buyerCapital.setBackgroundTintList(ColorStateList.valueOf(
                Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
        holder.sellername.setText(bean.getSellername());
        holder.selleraddress.setText(bean.getSelleraddress());
        holder.sellergstNo.setText("GST: " + bean.getSellergstNo());
        holder.sellerphone.setText("Ph: " + bean.getSellerphone());
        holder.buyername.setText(bean.getBuyername());
        holder.buyeraddress.setText(bean.getBuyeraddress());
        holder.buyerphone.setText("Ph: " + bean.getBuyerphone());
        holder.buyergstNo.setText("GST: " + bean.getBuyergstNo());
        holder.invoice_date.setText(bean.timestamp);
        holder.quantity.setText("#" + bean.getParticularbeans().size());
//        try {
//            long valueInRuppee = Long.parseLong(bean.getValue());
//            long valueInTax = Long.parseLong(bean.getTax());
//            long valueCgst = (valueInTax * Long.parseLong(bean.getCgst())) / 100;
//            long valueSgst = (valueInTax * Long.parseLong(bean.getSgst())) / 100;
//            long valueIgst = (valueInTax * Long.parseLong(bean.getIgst())) / 100;
//            holder.total.setText("₹" + (int) (valueInRuppee + (valueCgst + valueIgst + valueSgst)));
//        }catch (Exception e){
//            holder.total.setText("₹" + "0");
//
//        }
        holder.shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemClick(position);
            }
        });
        holder.deleteview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemDeleteClick(position);
            }
        });
        holder.editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivityUser, MainEditActivity.class);
                intent.putExtra("data", bean);
                mainActivityUser.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mainbeansFiltered = mainbeans;
                } else {
                    ArrayList<Mainbean> filteredList = new ArrayList<>();
                    for (Mainbean row : mainbeans) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.buyername.toLowerCase().contains(charString.toLowerCase())
                                || row.buyerphone.contains(charSequence)) {
                            Log.e("xxxxxxxxxxx", charString.toLowerCase() + " " + row.buyername);
                            filteredList.add(row);
                        }
                    }

                    mainbeansFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mainbeansFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mainbeansFiltered = (ArrayList<Mainbean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public int getItemCount() {
        return mainbeansFiltered.size();
    }

}
