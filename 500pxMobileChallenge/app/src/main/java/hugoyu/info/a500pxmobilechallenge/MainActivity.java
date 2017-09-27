package hugoyu.info.a500pxmobilechallenge;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue mRequestQueue;
    boolean loading = false;
    int nextPage = 1;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // view
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        initSwipeRefreshLayout();
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        initRecyclerView();

        mRequestQueue = Volley.newRequestQueue(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshPhotos();
            }
        });
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPhotos();
            }
        });
    }

    private void refreshPhotos() {
        nextPage = 1;
        loadNextPage(true);
    }

    private void initRecyclerView() {
        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ImageAdapter(new ArrayList<String>(), this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();


                    if (!loading) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount - 5) {
                            Log.d("Page Loading", String.valueOf(nextPage));
                            loadNextPage(false);
                        }
                    }
            }
        });
    }

    /**
     *
     * @param replace whether to replace newly fetched data, true when refreshing
     */
    private void loadNextPage(final boolean replace) {
        String url = getString(R.string.api_url_photos);
        url += "?feature=popular";
        url += "&image_size=4";
        url += "&consumer_key=" + getString(R.string.api_key);
        url += "&page=" + nextPage;

        loading = true;
        mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Page Loaded", String.valueOf(nextPage));

                            JSONArray jsonArray = response.getJSONArray("photos");
                            ArrayList<String> newData = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject myPhotoObj = jsonArray.getJSONObject(i);
                                if (myPhotoObj.getInt("category") != 4) { // if photo is not nude
                                    newData.add(myPhotoObj.getJSONArray("images").
                                            getJSONObject(0).getString("url"));
                                }
                            }

                            if (replace) {
                                mAdapter.replaceData(newData);
                            } else {
                                mAdapter.addData(newData);
                            }

                            loading = false;
                            mSwipeRefreshLayout.setRefreshing(false);
                            nextPage++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null));
    }
}
