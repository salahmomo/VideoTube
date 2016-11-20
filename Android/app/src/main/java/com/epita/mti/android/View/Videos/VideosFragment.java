package com.epita.mti.android.View.Videos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epita.mti.android.MainActivity;
import com.epita.mti.android.Model.Video;
import com.epita.mti.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salah on 17/01/2016.
 */
public class VideosFragment extends Fragment {

    private List<Video> videos = new ArrayList<>();
    private List<Video> videosFilter = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private VideosAdapter videosAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static String TAG = "VIDEOS";

    private int currentPos = 0;

    public VideosFragment() {
    }

    public void setVideos(List<Video> videoList) {
        this.videos = new ArrayList<>(videoList);
        videosFilter = new ArrayList<>(videos);
        videosAdapter.updateVideos(videoList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //get All Movies fromjson

        videosAdapter = new VideosAdapter(getContext(), videosFilter);
        videosAdapter.setOnItemClickListener(new VideosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String etag) {

                //go to movie element
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.gotToVideoFragment(position, etag);
                /*Intent intent = new Intent(getContext(), SweebiDetailsActivity.class);
                Movie canvas = movies.get(position);
                if (canvas != null)
                    intent.putExtra("canvas", canvas);
                startActivity(intent);*/
            }
        });

        mRecyclerView.setAdapter(videosAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("MainActivity", "lol");
        mRecyclerView.scrollToPosition(currentPos);
    }

    public boolean onQueryTextChange(String query) {
        final List<Video> tmpVideos = new ArrayList<>(videos);
        final List<Video> filteredVideoList = filter(tmpVideos, query);
        videosAdapter.animateTo(filteredVideoList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private List<Video> filter(List<Video> videos, String query) {
        query = query.toLowerCase();

        final List<Video> filteredVideoList = new ArrayList<>();
        for (Video video : videos) {
            final String text = video.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredVideoList.add(video);
            }
        }
        return filteredVideoList;
    }


    public void newCurrentPos(int pos)
    {
        currentPos = pos;
    }
    public List<Video> getVideos() {
        return videos;
    }


    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public VideosAdapter getVideosAdapter() {
        return videosAdapter;
    }

    public void setVideosAdapter(VideosAdapter videosAdapter) {
        this.videosAdapter = videosAdapter;
    }
}

