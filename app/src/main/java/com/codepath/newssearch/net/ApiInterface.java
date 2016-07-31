package com.codepath.newssearch.net;

import com.codepath.newssearch.models.ResponseWrapper;
import com.codepath.newssearch.models.SearchResultsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sharath on 7/30/16.
 */
public interface ApiInterface {

    @GET("articlesearch.json")
    Call<ResponseWrapper> getArticles(@Query("api-key") String apiKey
                                            , @Query("q") String searchQuery
                                            , @Query("page") String pageNumber
                                            , @Query("sort") String sortOrder
                                            , @Query("begin_date") String beginDate
                                            , @Query("fq") String categories);


}
