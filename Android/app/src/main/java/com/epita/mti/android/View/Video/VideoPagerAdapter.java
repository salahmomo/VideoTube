package com.epita.mti.android.View.Video;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.epita.mti.android.Model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salah on 17/01/2016.
 */
public class VideoPagerAdapter extends FragmentPagerAdapter {

    private List<Video> videos = new ArrayList<>();
    public VideoPagerAdapter(FragmentManager fm, List<Video> videos) {
        super(fm);
        this.videos = videos;
    }

    @Override
    public Fragment getItem(int position) {
        return VideoFragment.newInstance(videos.get(position));
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return videos.get(position).getTitle();
    }
}
