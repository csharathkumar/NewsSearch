package com.codepath.newssearch.actvities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.newssearch.R;
import com.codepath.newssearch.adapters.ArticleArrayAdapter;
import com.codepath.newssearch.models.Article;
import com.codepath.newssearch.models.SearchModel;
import com.codepath.newssearch.util.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH_MODEL = "search_criteria_model";
    public static final int GET_SEARCH_CRITERIA_REQUEST = 121;
    public static final String TAG = SearchActivity.class.getSimpleName();

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleArrayAdapter;
    SearchModel mSearchModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();

    }

    public void setUpViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        articleArrayAdapter = new ArticleArrayAdapter(this,articles);
        gvResults.setAdapter(articleArrayAdapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent to display the article
                Intent intent = new Intent(getApplicationContext(),ArticleActivity.class);
                //get article to display
                Article article = (Article) parent.getItemAtPosition(position);
                //pass article into intent
                intent.putExtra("url",article.getWebUrl());
                //launch the activity
                startActivity(intent);
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
                Toast.makeText(getApplicationContext(),"Filter item clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),FilterActivity.class);
                startActivityForResult(intent,GET_SEARCH_CRITERIA_REQUEST);
                return true;
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

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this,"Search Query entered is - "+query,Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Constants.BASE_URL;
        RequestParams params = new RequestParams();
        params.put("api-key",Constants.API_KEY);
        params.put("page",0);
        params.put("q",query);
        if(mSearchModel != null){
            params.put("begin_date",mSearchModel.getBeginDate());
            if(mSearchModel.getCategories() != null && !mSearchModel.getCategories().isEmpty()){
                String categories = "";
                for(String string : mSearchModel.getCategories()){
                    categories = categories+'"'+string+'"';
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
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    articleArrayAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }
}
