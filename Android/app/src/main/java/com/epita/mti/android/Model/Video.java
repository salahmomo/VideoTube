package com.epita.mti.android.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Salah on 18/01/2016.
 */
public class Video implements Parcelable {

    public static String TYPE_VIDEO = "youtube#video";
    public static String TYPE_PLAYLIST = "youtube#playlist";
    public static String TYPE_CHANNEL = "youtube#channel";
    public static String VIDEO_URL = "http://www.youtube.com/watch?v=";
    public static String PLAYLIST_URL = "http://www.youtube.com/playlist?list=";
    public static String CHANNEL_URL = "http://www.youtube.com/channel/";

    public String kind;
    public String etag;
    public VideoID id;
    public Snippet snippet;

    private String title;
    private String description;
    private String defaultThumbnailURL;
    private String mediumThumbnailURL;
    private String highThumbnailURL;
    private String publishDate;
    private String videoKind;
    private String videoId;
    private String playlistId;
    private String channelTitle;
    private String channelSnippetId;
    private String channelId;

    public void updateProperties() {
        this.title = snippet.title;
        this.description = snippet.description;
        this.defaultThumbnailURL = snippet.thumbnails.defaultThumbnail.url;
        this.mediumThumbnailURL = snippet.thumbnails.medium.url;
        this.highThumbnailURL = snippet.thumbnails.high.url;
        this.publishDate = snippet.publishedAt;
        this.videoKind = id.kind;
        this.videoId = id.videoId;
        this.playlistId = id.playlistId;
        this.channelTitle = snippet.channelTitle;
        this.channelSnippetId = snippet.channelId;
        this.channelId = id.channelId;
    }

    public String getVideoURL() {
        if (videoKind.equals(TYPE_VIDEO))
            return VIDEO_URL+videoId;
        if (videoKind.equals(TYPE_PLAYLIST))
            return PLAYLIST_URL+playlistId;
        if (videoKind.equals(TYPE_CHANNEL))
            return CHANNEL_URL+channelId;
        return "";
    }

    public String getChannelURL() {
        if (!channelSnippetId.equals(""))
            return CHANNEL_URL+channelSnippetId;
        return "";
    }

    public String getKind() {
        if (videoKind.equals(TYPE_VIDEO))
            return "Video";
        if (videoKind.equals(TYPE_PLAYLIST))
            return "Playlist";
        if (videoKind.equals(TYPE_CHANNEL))
            return "Channel";
        return "";
    }

    /**
     * For Parcelable interface
     */

    protected Video(Parcel in) {
        etag = in.readString();
        title = in.readString();
        description = in.readString();
        defaultThumbnailURL = in.readString();
        mediumThumbnailURL = in.readString();
        highThumbnailURL = in.readString();
        publishDate = in.readString();
        videoKind = in.readString();
        videoId = in.readString();
        playlistId = in.readString();
        channelTitle = in.readString();
        channelSnippetId = in.readString();
        channelId = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(etag);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(defaultThumbnailURL);
        dest.writeString(mediumThumbnailURL);
        dest.writeString(highThumbnailURL);
        dest.writeString(publishDate);
        dest.writeString(videoKind);
        dest.writeString(videoId);
        dest.writeString(playlistId);
        dest.writeString(channelTitle);
        dest.writeString(channelSnippetId);
        dest.writeString(channelId);
    }

    /**
     * getter/setter
     */
    public String getTitle() {
        return title;
    }
    public String getDescription() { return description; }
    public String getDefaultThumbnailURL() {
        return defaultThumbnailURL;
    }
    public String getMediumThumbnailURL() {
        return mediumThumbnailURL;
    }
    public String getHighThumbnailURL() {
        return highThumbnailURL;
    }
    public String getPublishDate() { return publishDate; }
    public String getVideoKind() { return videoKind; }
    public String getVideoId() { return videoId; }
    public String getPlaylistId() { return playlistId; }
    public String getChannelTitle() { return channelTitle; }
    public String getChannelSnippetId() { return channelSnippetId; }
    public String getChannelId() {
        return channelId;
    }
}

