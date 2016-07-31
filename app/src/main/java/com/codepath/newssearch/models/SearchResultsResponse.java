package com.codepath.newssearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharath on 7/30/16.
 */
public class SearchResultsResponse {

    @SerializedName("docs")
    @Expose
    private List<Article> articles = new ArrayList<Article>();
    @SerializedName("meta")
    @Expose
    private Meta meta;

    /**
     *
     * @return
     * The docs
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     *
     * @param articles
     * The docs
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     *
     * @return
     * The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     *
     * @param meta
     * The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
