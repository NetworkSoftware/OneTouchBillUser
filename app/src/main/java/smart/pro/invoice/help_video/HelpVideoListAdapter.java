package smart.pro.invoice.help_video;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.internal.exoplayer.Videobean;

import java.util.ArrayList;

import smart.pro.invoice.R;
import smart.pro.invoice.app.AppConfig;
import smart.pro.invoice.app.GlideApp;


public class HelpVideoListAdapter extends RecyclerView.Adapter<HelpVideoListAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Videobean> videobeans;
    SharedPreferences preferences;
    public OnVideoClick videoClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView itemsDuration;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super((view));
            title = view.findViewById(R.id.title);
            itemsDuration = view.findViewById(R.id.itemsDuration);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }

    public HelpVideoListAdapter(Context mainActivityUser, ArrayList<Videobean> videobeans, OnVideoClick videoClick) {
        this.mainActivityUser = mainActivityUser;
        this.videobeans = videobeans;
        this.videoClick = videoClick;
    }

    public void notifyData(ArrayList<Videobean> myorderBeans) {
        this.videobeans = myorderBeans;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videoplay_list, parent, false);

        return new HelpVideoListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Videobean videobean = videobeans.get(position);


        holder.title.setText(videobean.getTitle());
        Log.e("Xxxxxxx", AppConfig.IMAGEPATH + videobean.thumbnail);
        GlideApp.with(mainActivityUser)
                .load(AppConfig.IMAGEPATH + videobean.thumbnail)
                .into(holder.thumbnail);
        holder.itemsDuration.setText(videobean.duration);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoClick.itemClick(position);
            }
        });


    }

    public int getItemCount() {
        return videobeans.size();
    }

}


