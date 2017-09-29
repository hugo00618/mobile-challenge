package hugoyu.info.a500pxmobilechallenge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.adapter.GalleryAdapter;
import hugoyu.info.a500pxmobilechallenge.model.MyGallery;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;

    MyGallery myGallery;
    GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        initSwipeRefreshLayout();
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        initRecyclerView();

        // refresh photos
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshPhotos();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                mRecyclerView.scrollToPosition(position);
            }
        }
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
        myGallery.loadNextPage(true, mAdapter, mSwipeRefreshLayout);
    }

    private void initRecyclerView() {
        myGallery = MyGallery.getInstance(this, Volley.newRequestQueue(this));

        final FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new GalleryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                // if scrolled to the last 5 images, load next page
                if ((visibleItemCount + firstVisibleItem) >= totalItemCount - 5) {
                    myGallery.loadNextPage(false, mAdapter, null);
                }
            }
        });
    }


}
