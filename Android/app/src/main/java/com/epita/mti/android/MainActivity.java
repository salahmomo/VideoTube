package com.epita.mti.android;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.epita.mti.android.API.SearchObjectTask;
import com.epita.mti.android.API.APICallback;
import com.epita.mti.android.Model.SearchObject;
import com.epita.mti.android.Model.Video;
import com.epita.mti.android.View.SearchResultsActivity;
import com.epita.mti.android.View.Team.TeamActivity;
import com.epita.mti.android.View.Video.VideoPagerFragment;
import com.epita.mti.android.View.Videos.VideosFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private VideoPagerFragment videoPagerFragment = null;
    private VideosFragment videoFragment = null;
    private List<Video> videos = new ArrayList<>();
    private Toolbar toolbar;

    private MenuItem searchItem;
    private MenuItem shareItem;
    private MenuItem playItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle("VideoTube");

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            videoFragment = (VideosFragment) fragmentManager.findFragmentByTag(VideosFragment.TAG);
            if (videoFragment == null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                videoFragment = new VideosFragment();
                fragmentTransaction.add(R.id.fragment, videoFragment, VideosFragment.TAG);
                fragmentTransaction.commit();
            }

            final LinearLayout progressBar = (LinearLayout) findViewById(R.id.globalProgress);
            progressBar.setVisibility(View.VISIBLE);

            new SearchObjectTask().setCallback(new APICallback() {
                @Override
                public void call(SearchObject result) {
                    for (Video v : result.items)
                        v.updateProperties();

                    videos = result.items;
                    videoFragment.setVideos(result.items);
                    progressBar.setVisibility(View.GONE);
                }
            }).execute("");
        }
    }

    public void gotToVideoFragment(int posVideo, String etag){
        FragmentManager fragmentManager = getSupportFragmentManager();

        videoPagerFragment = (VideoPagerFragment) fragmentManager.findFragmentByTag(VideoPagerFragment.TAG);
        if (videoPagerFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            videoPagerFragment = VideoPagerFragment.newInstance(posVideo, etag);
            fragmentTransaction.replace(R.id.fragment, videoPagerFragment, VideoPagerFragment.TAG);
            fragmentTransaction.addToBackStack("video");
            fragmentTransaction.hide(videoFragment);
            fragmentTransaction.commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            videoPagerFragment = (VideoPagerFragment) fragmentManager.findFragmentByTag(VideoPagerFragment.TAG);
            int pos = 0;
            if (videoPagerFragment != null)
            {
                pos = videoPagerFragment.getmViewPager().getCurrentItem();
            }
            videoFragment.newCurrentPos(pos);
            fragmentManager.popBackStack();
            searchItem.setVisible(true);
            shareItem.setVisible(false);
            playItem.setVisible(false);
            setActionBarTitle("VideoTube");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        shareItem = menu.findItem(R.id.action_share);
        playItem = menu.findItem(R.id.action_play);
        shareItem.setVisible(false);
        playItem.setVisible(false);
        searchItem.setVisible(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchResultsActivity.class)));
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        videoFragment.onQueryTextChange(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_team:
                Intent intent = new Intent(getBaseContext(), TeamActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public List<Video> getVideos()
    {
        return videos;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
