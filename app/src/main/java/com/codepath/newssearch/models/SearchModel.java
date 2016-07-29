package com.codepath.newssearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Sharath on 7/28/16.
 */
public class SearchModel implements Parcelable {
    String sortOrder;
    String beginDate;
    List<String> categories;

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sortOrder);
        dest.writeString(this.beginDate);
        dest.writeStringList(this.categories);
    }

    public SearchModel() {
    }

    protected SearchModel(Parcel in) {
        this.sortOrder = in.readString();
        this.beginDate = in.readString();
        this.categories = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SearchModel> CREATOR = new Parcelable.Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel source) {
            return new SearchModel(source);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };
}
