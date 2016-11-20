package com.epita.mti.android.View.Videos;

import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epita.mti.android.API.DownloadThumbnailTask;
import com.epita.mti.android.Model.Video;
import com.epita.mti.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salah on 17/01/2016.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>{

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, String etag);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public ImageView thumbnail;
        public TextView channelTitle;
        public TextView typeItem;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            channelTitle = (TextView) v.findViewById(R.id.channel_title);
            typeItem = (TextView) v.findViewById(R.id.type_item);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myOnItemClickListener != null)
                myOnItemClickListener.onItemClick(v, getAdapterPosition(), videos.get(getAdapterPosition()).etag);
        }
    }

    private Context context;
    private List<Video> videos = new ArrayList<>();
    OnItemClickListener myOnItemClickListener;

    public VideosAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_videos, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = videos.get(position);

        holder.title.setText(video.getTitle());
        holder.channelTitle.setText(video.getChannelTitle());

        holder.typeItem.setText(video.getKind());

        Picasso.with(context).load(video.getHighThumbnailURL()).placeholder(R.drawable.yt_logo).into(holder.thumbnail);
        /*if (holder.thumbnail.getDrawable() == null)
            Picasso.with(context).load(R.drawable.yt_logo).into(holder.thumbnail);*/
        holder.thumbnail.setColorFilter(Color.argb(75, 0, 0, 0));
    }
    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener){
        this.myOnItemClickListener = onItemClickListener;
    }

    public void setVideos(List<Video> models) {
        videos = new ArrayList<>(models);
    }

    public Video removeItem(int position) {
        final Video video = videos.remove(position);
        notifyItemRemoved(position);
        return video;
    }

    public void addItem(int position, Video video) {
        videos.add(position, video);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Video video = videos.remove(fromPosition);
        videos.add(toPosition, video);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Video> videos) {
        applyAndAnimateRemovals(videos);
        applyAndAnimateAdditions(videos);
        applyAndAnimateMovedItems(videos);
    }

    private void applyAndAnimateRemovals(List<Video> newVideos) {
        for (int i = videos.size() - 1; i >= 0; i--) {
            final Video model = videos.get(i);
            if (!newVideos.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Video> newVideos) {
        for (int i = 0, count = newVideos.size(); i < count; i++) {
            final Video video = newVideos.get(i);
            if (!videos.contains(video)) {
                addItem(i, video);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Video> newVideos) {
        for (int toPosition = newVideos.size() - 1; toPosition >= 0; toPosition--) {
            final Video video = newVideos.get(toPosition);
            final int fromPosition = videos.indexOf(video);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void updateVideos(List<Video> videos) {
        this.videos = videos;
        this.notifyDataSetChanged();
    }
}