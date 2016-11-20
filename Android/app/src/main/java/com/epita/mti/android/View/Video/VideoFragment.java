package com.epita.mti.android.View.Video;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epita.mti.android.Config;
import com.epita.mti.android.MainActivity;
import com.epita.mti.android.Model.Video;
import com.epita.mti.android.R;
import com.epita.mti.android.View.Team.TeamActivity;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Salah on 17/01/2016.
 */
public class VideoFragment extends Fragment implements View.OnClickListener{

    private Video video;

    private ImageView cover;
    private TextView title;
    private TextView description;
    private TextView channel;
    private TextView publish;

    public VideoFragment() {
    }


    private static final String ARG_VIDEO = "video";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VideoFragment newInstance(Video video) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIDEO, video);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        if (getArguments() != null) {
            video = getArguments().getParcelable(ARG_VIDEO);
        }
        cover = (ImageView)  view.findViewById(R.id.cover);
        Picasso.with(getContext()).load(video.getHighThumbnailURL()).placeholder(R.drawable.yt_logo).into(cover);

        RelativeLayout layoutCover = (RelativeLayout) view.findViewById(R.id.layout_cover);
        layoutCover.setOnClickListener(this);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(video.getTitle());

        description = (TextView) view.findViewById(R.id.description);
        description.setText(video.getDescription());

        channel = (TextView) view.findViewById(R.id.channel_title);
        if (!video.getChannelTitle().equals(""))
            channel.setText(video.getChannelTitle());

        LinearLayout channelLayout = (LinearLayout) view.findViewById(R.id.channel_layout);
        channelLayout.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        publish = (TextView) view.findViewById(R.id.publish);
        try {
            Date date = sdf.parse(video.getPublishDate());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            String formatDate = "Published on " + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            publish.setText(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.layout_cover:
                Intent intent;
                String url = video.getVideoURL();
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
                return;
            case R.id.channel_layout:
                url = video.getChannelURL();
                if (url != "") {
                    try {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.google.android.youtube");
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
                return;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        MenuItem playItem = menu.findItem(R.id.action_play);
        searchItem.setVisible(false);
        shareItem.setVisible(true);
        if (video.getVideoKind().equals(Video.TYPE_VIDEO))
            playItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_share:
                shareActualContent();
                break;
            case R.id.action_play:
                playEmbeddedPlayer();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareActualContent()
    {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, video.getTitle());
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, video.getVideoURL());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public void playEmbeddedPlayer()
    {
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getActivity()).equals(YouTubeInitializationResult.SUCCESS)) {
            if (video.getVideoKind().equals(Video.TYPE_VIDEO)) {
                startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                        Config.YOUTUBE_API_KEY, video.getVideoId(), 0, true, true));
            }
        }
        else
        {
            Intent intent;
            String url = video.getVideoURL();
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
