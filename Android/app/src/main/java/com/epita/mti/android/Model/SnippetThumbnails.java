package com.epita.mti.android.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VisionElf on 19/01/2016.
 */
public class SnippetThumbnails {

    @SerializedName("default")
    public Thumbnail defaultThumbnail;
    public Thumbnail medium;
    public Thumbnail high;
}
