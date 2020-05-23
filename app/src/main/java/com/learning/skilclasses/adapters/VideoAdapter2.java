package com.learning.skilclasses.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.learning.skilclasses.R;
import com.learning.skilclasses.fragments.video_player;
import com.learning.skilclasses.models.Video;
import com.learning.skilclasses.preferences.UserSession;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.OkHttpClient;

public class VideoAdapter2 extends RecyclerView.Adapter<VideoAdapter2.ViewHolder> implements Filterable {

    private List<Video> videoList;
    private List<Video> listFull;
    private Context context;
    UserSession userSession;
    private OkHttpClient okHttpClient;

    public VideoAdapter2(List<Video> messageList, Context context) {
        this.videoList = messageList;
        this.context = context;
        userSession = new UserSession(context);
        this.listFull = new ArrayList<>(videoList);
    }

    @NonNull
    @Override
    public VideoAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout_2, parent, false);
        return new VideoAdapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter2.ViewHolder holder, int position) {
        Video video = videoList.get(position);
        try {
            holder.title.setText(video.getVdesp());
            String sub = video.getVsubject();
            holder.videoSuject.setText("[ " + sub.substring(0, 1).toUpperCase() + sub.substring(1) + " ]");
            Picasso.get().load(video.getThumbnailimg()).into(holder.imgV);

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new video_player(videoList.get(position),context);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        @BindView(R.id.video_title)
        TextView title;
        @BindView(R.id.video_subject)
        TextView videoSuject;
        @BindView(R.id.videoplayerimg)
        ImageView imgV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            card=itemView.findViewById(R.id.cardview);
        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Video> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Video items : listFull) {
                    if (items.getVdesp().toLowerCase().contains(filterPattern)) {
                        filteredList.add(items);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            videoList.clear();
            videoList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}

