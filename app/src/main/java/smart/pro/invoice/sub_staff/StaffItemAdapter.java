package smart.pro.invoice.sub_staff;

import android.content.Context;
import android.content.Intent;
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

import smart.pro.invoice.MainEditActivity;
import smart.pro.invoice.OnItemClick;
import smart.pro.invoice.R;

public class StaffItemAdapter extends RecyclerView.Adapter<StaffItemAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Staffbean> staffbeans;
    private OnItemClick onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView staff_name,staff_phone;
        ImageView deleteview, editview;

        public MyViewHolder(View view) {
            super((view));

            staff_name = (TextView) view.findViewById(R.id.staff_name);
            staff_phone = (TextView) view.findViewById(R.id.staff_phone);
            deleteview = (ImageView) view.findViewById(R.id.deleteview);
            editview = (ImageView) view.findViewById(R.id.editview);

        }
    }

    public StaffItemAdapter(Context mainActivityUser, ArrayList<Staffbean> staffbeans, InviteStaff onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.staffbeans = staffbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Staffbean> staffbeans) {
        this.staffbeans = staffbeans;
        notifyDataSetChanged();
    }

    public StaffItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_list_item, parent, false);

        return new StaffItemAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(StaffItemAdapter.MyViewHolder holder, final int position) {
        Staffbean staffbean = staffbeans.get(position);
        if (staffbean.staff_name.length()>0) {
            holder.staff_name.setText(String.valueOf(staffbean.staff_name.charAt(0)).toUpperCase());
        }else{
            holder.staff_name.setText("B");

        }
        Random rnd = new Random();
        holder.staff_name.setBackgroundTintList(ColorStateList.valueOf(
                Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))));
        holder.staff_name.setText(staffbean.getStaff_name());
        holder.staff_phone.setText(staffbean.getStaff_phone());

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
                intent.putExtra("data", staffbean);
                mainActivityUser.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return staffbeans.size();
    }

}
