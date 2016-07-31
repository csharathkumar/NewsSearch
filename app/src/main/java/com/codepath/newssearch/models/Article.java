package com.codepath.newssearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharath on 7/26/16.
 */
public class Article implements Parcelable {
    private static final String TAG = Article.class.getSimpleName();
    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("headline")
    @Expose
    private Headline headline;
    @SerializedName("multimedia")
    @Expose
    private List<Multimedia> multimedia = new ArrayList<Multimedia>();

    private String thumbNail = "";

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeParcelable(this.headline, flags);
        dest.writeList(this.multimedia);
        dest.writeString(this.thumbNail);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readParcelable(Headline.class.getClassLoader());
        this.multimedia = new ArrayList<Multimedia>();
        in.readList(this.multimedia, Multimedia.class.getClassLoader());
        this.thumbNail = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
