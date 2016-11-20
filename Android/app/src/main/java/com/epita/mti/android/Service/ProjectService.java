package com.epita.mti.android.Service;

import com.epita.mti.android.Model.SearchObject;
import com.epita.mti.android.Model.Video;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by VisionElf on 19/01/2016.
 */
public interface ProjectService {
    public static final String ENDPOINT = "http://tutos-android.com/MTI/2016/Projet/";

    @GET("/search")
    SearchObject listVideos();
}
