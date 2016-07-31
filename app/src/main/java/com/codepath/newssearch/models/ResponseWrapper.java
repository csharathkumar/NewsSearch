package com.codepath.newssearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sharath on 7/30/16.
 */
public class ResponseWrapper {
    @SerializedName("response")
    @Expose
    private SearchResultsResponse response;
    /**
     *
     * @return
     * The response
     */
    public SearchResultsResponse getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The response
     */
    public void setResponse(SearchResultsResponse response) {
        this.response = response;
    }
}
