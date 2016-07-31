package com.codepath.newssearch.models;

/**
 * Created by Sharath on 7/30/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Multimedia implements Parcelable {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("copyright")
    @Expose
    private String copyright;

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     *
     * @return
     * The format
     */
    public String getFormat() {
        return format;
    }

    /**
     *
     * @param format
     * The format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     *
     * @return
     * The height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return
     * The width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     *
     * @param subtype
     * The subtype
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     *
     * @return
     * The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     *
     * @param copyright
     * The copyright
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.format);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeString(this.type);
        dest.writeString(this.subtype);
        dest.writeString(this.caption);
        dest.writeString(this.copyright);
    }

    public Multimedia() {
    }

    protected Multimedia(Parcel in) {
        this.url = in.readString();
        this.format = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
        this.type = in.readString();
        this.subtype = in.readString();
        this.caption = in.readString();
        this.copyright = in.readString();
    }

    public static final Parcelable.Creator<Multimedia> CREATOR = new Parcelable.Creator<Multimedia>() {
        @Override
        public Multimedia createFromParcel(Parcel source) {
            return new Multimedia(source);
        }

        @Override
        public Multimedia[] newArray(int size) {
            return new Multimedia[size];
        }
    };
}