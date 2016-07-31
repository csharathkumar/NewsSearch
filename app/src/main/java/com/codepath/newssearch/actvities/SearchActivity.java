package com.codepath.newssearch.actvities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.codepath.newssearch.R;
import com.codepath.newssearch.adapters.ArticleArrayAdapter;
import com.codepath.newssearch.adapters.ArticlesRecyclerAdapter;
import com.codepath.newssearch.fragments.FilterDialogFragment;
import com.codepath.newssearch.models.Article;
import com.codepath.newssearch.models.ResponseWrapper;
import com.codepath.newssearch.models.SearchModel;
import com.codepath.newssearch.models.SearchResultsResponse;
import com.codepath.newssearch.net.ApiClient;
import com.codepath.newssearch.net.ApiInterface;
import com.codepath.newssearch.util.Constants;
import com.codepath.newssearch.util.EndlessRecyclerViewScrollListener;
import com.codepath.newssearch.util.EndlessScrollListener;
import com.codepath.newssearch.util.NetworkUtils;
import com.codepath.newssearch.util.SpacesItemDecoration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterFragmentListener {
    public static final String EXTRA_SEARCH_MODEL = "search_criteria_model";
    public static final int GET_SEARCH_CRITERIA_REQUEST = 121;
    public static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.etQuery) EditText etQuery;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.rvArticles) RecyclerView rvArticles;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;

    String mQuery;
    int mTotalResults;
    int mCurrentPage;
    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;
    ArticlesRecyclerAdapter articlesRecyclerAdapter;
    SearchModel mSearchModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        setUpViews();
        etQuery.setText("android");
    }

    public void setUpViews(){
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this,articles);
        articlesRecyclerAdapter = new ArticlesRecyclerAdapter(this,articles);
        articlesRecyclerAdapter.setOnItemClickListener(new ArticlesRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View itemView, int position) {
                // create an intent to display the article
                //Intent intent = new Intent(getApplicationContext(),ArticleActivity.class);
                //get article to display
                Article article = articles.get(position);
                //pass article into intent
                //intent.putExtra(ArticleActivity.EXTRA_ARTICLE,article);
                //launch the activity
                //startActivity(intent);
                launchChromeCustomTab(article);
            }
        });
        rvArticles.setAdapter(articlesRecyclerAdapter);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(16);
        rvArticles.addItemDecoration(spacesItemDecoration);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,1);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);

        rvArticles.setOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                sendRequestUsingRetrofit(page,mQuery);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search,menu);
        MenuItem filterItem = menu.findItem(R.id.filter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //show the filter dialog
                FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance();
                filterDialogFragment.show(getSupportFragmentManager(),"Filter Dialog");
                return true;
            }
        });
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                searchItem.collapseActionView();
                mQuery = query;
                sendRequestUsingRetrofit(0,query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_SEARCH_CRITERIA_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    mSearchModel = data.getParcelableExtra(EXTRA_SEARCH_MODEL);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showSnackBar(final int pageNumber, final String query){
        snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequestUsingRetrofit(pageNumber,query);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
    public void onArticleSearch(View view) {
        mQuery = etQuery.getText().toString();
        sendRequestUsingRetrofit(0,mQuery);
    }
    private void sendRequestUsingRetrofit(final int pageNumber, String query){
        if(NetworkUtils.isOnline()){
            if(snackbar != null){
                if(snackbar.isShown()){
                    snackbar.dismiss();
                }
            }
            String beginDate = null;
            String categories = null;
            String sortOrder = null;
            if(mSearchModel != null){
                if(mSearchModel.getBeginDate() != null){
                    beginDate = mSearchModel.getBeginDate();
                }
                if(mSearchModel.getSortOrder() != null){
                    sortOrder = mSearchModel.getSortOrder();
                }
                List<String> categoriesList = mSearchModel.getCategories();
                if(!categoriesList.isEmpty()){
                    categories = "news_desk:(";
                    for(int i=0;i<categoriesList.size()-1;i++){
                        categories = categories+'"'+categoriesList.get(i)+'"'+" ";
                    }
                    categories = categories+'"'+categoriesList.get(categoriesList.size()-1)+'"'+")";
                }
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseWrapper> searchResultsResponseCall =
                    apiInterface.getArticles(Constants.API_KEY,query,String.valueOf(pageNumber),sortOrder,beginDate,categories);
            searchResultsResponseCall.enqueue(new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                    SearchResultsResponse searchResultsResponse = response.body().getResponse();
                    List<Article> articlesReturned = searchResultsResponse.getArticles();
                    if(pageNumber == 0){
                        articles.clear();
                    }
                    articles.addAll(articlesReturned);
                    mTotalResults = searchResultsResponse.getMeta().getHits();
                    articlesRecyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    Log.e(TAG,"Failed to fetch results for the given search query.");
                }
            });
        }else{
            showSnackBar(pageNumber, query);
        }

    }
    private void sendRequest(final int pageNumber, String query){
        mCurrentPage = pageNumber;
        //String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Constants.BASE_URL;
        RequestParams params = new RequestParams();
        params.put("api-key",Constants.API_KEY);
        params.put("page",pageNumber);
        params.put("q",query);
        if(mSearchModel != null){
            params.put("begin_date",mSearchModel.getBeginDate());
            List<String> categoriesList = mSearchModel.getCategories();
            if(!categoriesList.isEmpty()){
                String categories = "news_desk:(";
                for(int i=0;i<categoriesList.size()-1;i++){
                    categories = categories+'"'+categoriesList.get(i)+'"'+" ";
                }
                categories = categories+'"'+categoriesList.get(categoriesList.size()-1)+'"'+")";
                try {
                    params.put("fq", URLEncoder.encode(categories,"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            params.put("sort",mSearchModel.getSortOrder());
        }
        Log.d(TAG,"URL sent is - "+url);
        client.get(url, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray articleJsonResults = null;
                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    JSONObject metaJsonObject = response.getJSONObject("response").getJSONObject("meta");
                    mTotalResults = Integer.parseInt(metaJsonObject.getString("hits"));
                    Log.d(TAG,"Total results is - "+mTotalResults);
                    if(pageNumber == 0){
                        articles.clear();
                    }
                    //articles.addAll(Article.fromJSONArray(articleJsonResults));
                    articleArrayAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG,"Error response received - ");
            }
        });
    }
    @Override
    public void onFiltersSelected(SearchModel searchModel) {
        mSearchModel = searchModel;
    }

    private void launchChromeCustomTab(Article article){
        String url = article.getWebUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(true);
        String shareLabel = getString(R.string.share_label);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_menu_share);
        PendingIntent pendingIntent = createPendingShareIntent(url);
        builder.setActionButton(icon, shareLabel, pendingIntent);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(SearchActivity.this, Uri.parse(url));
    }

    private PendingIntent createPendingShareIntent(String url) {
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("text/plain");
        actionIntent.putExtra(Intent.EXTRA_TEXT, url);
        return PendingIntent.getActivity(
                getApplicationContext(), 0, actionIntent, 0);
    }
}
