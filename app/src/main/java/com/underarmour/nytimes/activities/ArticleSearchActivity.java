package com.underarmour.nytimes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.underarmour.nytimes.R;
import com.underarmour.nytimes.adapters.ArticleAdapter;
import com.underarmour.nytimes.models.Article;
import com.underarmour.nytimes.models.ResponseObj;
import com.underarmour.nytimes.models.SearchApiResponse;
import com.underarmour.nytimes.network.ISearchApiEndpoint;
import com.underarmour.nytimes.network.RestClient;
import com.underarmour.nytimes.storage.DataStorage;
import com.underarmour.nytimes.uiutils.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ArticleAdapter.CustomOnClickListener {

    private static final String TAG = ArticleSearchActivity.class.getSimpleName();
    private static final String ARTICLE_TEXT = "article";
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private TextView tvEmptyList;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ArrayList<Article> listArticles;
    private TextView tvLoadMore;
    String query;
    private DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayShowTitleEnabled(false);
        dataStorage = DataStorage.getInstance();
        // api call w/ default query
        getSearchedArticles(query,"0");

        setViews();
    }

    /**
     * initialize the views
     */
    private void setViews() {

        tvEmptyList = (TextView) findViewById(R.id.tvEmptyList);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        listArticles = new ArrayList<Article>();
        loadViews(listArticles);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {

            Intent intent = new Intent(this,SearchFilterActivity.class);
            startActivity(intent);
            return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        searchView.clearFocus();
        String page = "0";
        this.query = query;
        showScrollMore();
        getSearchedArticles(query,page);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void loadViews(ArrayList<Article> articleList) {

        tvEmptyList.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        StaggeredGridLayoutManager staggeredGridLayout= new StaggeredGridLayoutManager(2,1);
     //   recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(staggeredGridLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayout){

            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                view.post(new Runnable() {
                    @Override
                    public void run() {

                        loadMoreArticles(page);
                    }
                });

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        adapter = new ArticleAdapter(this,articleList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    /**
     * call api for loading new page data
     * @param page
     */
    private void loadMoreArticles(int page) {

        String pageText = String.valueOf(page);
        getSearchedArticles(query,pageText);
    }

    /**
     *  call API to get articles for a given query
     * @param query
     * @param page
     */
    private void getSearchedArticles(String query,String page) {

        ISearchApiEndpoint searchApiEndpoint = RestClient.createService();

        String sort= dataStorage.getData("Sort");
        if(sort==null||sort.isEmpty())
            sort = "Newest";
        List<String> listNewsDesk = (ArrayList<String>)dataStorage.getObjectData("Newsdesk");
        String filteredQ = listNewsDeskToString(listNewsDesk);
        Call<SearchApiResponse>  call= searchApiEndpoint.getArticles(query,filteredQ,sort,page);
        Callback<SearchApiResponse> callback =new SearchCallback();
        call.enqueue(callback);

    }

    public class SearchCallback implements Callback<SearchApiResponse>{

        @Override
        public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {

            SearchApiResponse apiResponse = response.body();
            if(apiResponse!=null){

                // parse response field from json obj
                ResponseObj responseObj = apiResponse.getResponse();
                ArrayList<Article> responseList = responseObj.getListArticles();
                if(listArticles!=null){
                    listArticles.addAll(responseList);
                    adapter.notifyDataSetChanged();
                }else {
                    listArticles = responseObj.getListArticles();
                }

            }else{
                // response is null
                showAllDataLoaded();
                String noResponse = getString(R.string.no_response);
                Log.d(TAG,noResponse);
            }
        }

        @Override
        public void onFailure(Call<SearchApiResponse> call, Throwable t) {

            String comm_failure = getString(R.string.comm_failure);
            Toast.makeText(ArticleSearchActivity.this,comm_failure,Toast.LENGTH_LONG).show();
            Log.d(TAG,comm_failure);
        }
    }

    /**
     *
     * @return a string from newsdesk values in list
     */
    private String listNewsDeskToString(List<String> listNewsDesk) {


        if(listNewsDesk!=null && listNewsDesk.size()>0) {

            String filterQ = "news_desk:(";
            for (int i = 0; i < listNewsDesk.size(); i++) {

                filterQ = filterQ + listNewsDesk.get(i) + "%20";

            }
            filterQ = filterQ + ")";
            return filterQ;
        }
        return null;
    }

    /**
     * text indicating user to read more articles
     */
    private void showScrollMore(){

        if(listArticles!=null){

            listArticles.clear();
            adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
            scrollListener.resetState();
        }
        String loadData = getString(R.string.load_more);
        tvLoadMore.setText(loadData);
    }

    /**
     * show text after all articles from server are loaded,
     * for a given search query
     */
    private void showAllDataLoaded(){

        String noData = getString(R.string.no_more_data);
        tvLoadMore.setText(noData);
    }

    @Override
    public void onItemClick(View view, int position) {

        Article article = listArticles.get(position);
        Intent intent = new Intent(this,DetailedArticleActivity.class);
        intent.putExtra(ARTICLE_TEXT, Parcels.wrap(article));
        startActivity(intent);
    }
}
