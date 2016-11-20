package com.epita.mti.android.API;

import android.os.AsyncTask;
import android.util.Log;

import com.epita.mti.android.Model.SearchObject;
import com.epita.mti.android.Service.ProjectService;

import retrofit.RestAdapter;

/**
 * Created by VisionElf on 19/01/2016.
 */
public class SearchObjectTask extends AsyncTask<String, Void, SearchObject> {

    APICallback callback;

    public SearchObjectTask setCallback(APICallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    protected SearchObject doInBackground(String... params) {
        ProjectService projectService = new RestAdapter.Builder()
                .setEndpoint(ProjectService.ENDPOINT)
                .build()
                .create(ProjectService.class);
        SearchObject searchObject = projectService.listVideos();
        return searchObject;
    }

    @Override
    public void onPostExecute(SearchObject result) {
        Log.i("OnPostExecute", "done, " + result.items.get(0).snippet.thumbnails.defaultThumbnail.url);
        callback.call(result);
    }

}
