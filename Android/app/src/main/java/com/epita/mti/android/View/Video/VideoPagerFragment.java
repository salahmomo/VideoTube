package com.epita.mti.android.View.Video;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epita.mti.android.MainActivity;
import com.epita.mti.android.Model.Video;
import com.epita.mti.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salah on 18/01/2016.
 */
public class VideoPagerFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private VideoPagerAdapter mVideoPagerAdapter;
    private ViewPager mViewPager;
    private List<Video> videos = new ArrayList<>();
    private int posVideo = 0;
    private String posEtag = "";

    private static final String ARG_POS_VIDEO = "pos_video";
    private static final String ARG_POS_TITLE = "pos_title";
    private static final String ARG_POS_ETAG = "pos_etag";
    private static final String ARG_VIDEOS = "videos";
    public static String TAG = "VIDEO_PAGER";

    public VideoPagerFragment() {
    }

    public static VideoPagerFragment newInstance(int posVideo, String posEtag) {
        VideoPagerFragment fragment = new VideoPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS_VIDEO, posVideo);
        args.putString(ARG_POS_ETAG, posEtag);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_pager, container, false);


        MainActivity mainActivity = (MainActivity) getActivity();
        videos = mainActivity.getVideos();

        if (getArguments() != null) {
            posVideo = getArguments().getInt(ARG_POS_VIDEO);
            posEtag = getArguments().getString(ARG_POS_ETAG);
        }

        mVideoPagerAdapter = new VideoPagerAdapter(getChildFragmentManager(), videos);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mVideoPagerAdapter);
        mViewPager.setCurrentItem(positionOfVideoWithEtag(posEtag));
        mViewPager.addOnPageChangeListener(this);
        setTitleFragment(videos.get(positionOfVideoWithEtag(posEtag)));


        return view;
    }

    public void goToPage(int pageNumber){
        mViewPager.setCurrentItem(pageNumber);
    }

    private int positionOfVideoWithEtag(String etag)
    {
        int pos = 0;
        for (Video video : videos){
            if (video.etag.equals(etag))
                break;
            pos++;
        }
        return pos;
    }

    public VideoPagerAdapter getmVideoPagerAdapter() {
        return mVideoPagerAdapter;
    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleFragment(videos.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setTitleFragment(Video video)
    {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (video.getVideoKind().equals(Video.TYPE_CHANNEL))
            mainActivity.setActionBarTitle("Channel");
        if (video.getVideoKind().equals(Video.TYPE_PLAYLIST))
            mainActivity.setActionBarTitle("Playlist");
        if (video.getVideoKind().equals(Video.TYPE_VIDEO))
            mainActivity.setActionBarTitle("Video");

    }
}
