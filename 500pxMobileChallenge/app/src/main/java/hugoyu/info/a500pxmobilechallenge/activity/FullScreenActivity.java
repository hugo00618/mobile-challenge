package hugoyu.info.a500pxmobilechallenge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.adapter.FullScreenAdapter;
import hugoyu.info.a500pxmobilechallenge.model.MyGallery;

/**
 * Created by Hugo on 2017-09-27.
 */

public class FullScreenActivity extends AppCompatActivity {

    ViewPager mViewPager;
    FullScreenAdapter mAdapter;
    MyGallery myGallery;

    boolean dirtyBit = false; // whether full screen has changed MyGallery data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        getSupportActionBar().hide();

        myGallery = MyGallery.getInstance(this, Volley.newRequestQueue(this));

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mAdapter = new FullScreenAdapter(this);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                if (position >= MyGallery.getData().size() - 5) {
                    myGallery.loadNextPage(false, mAdapter, null);
                    dirtyBit = true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // scroll to tapped image
        mViewPager.setCurrentItem(getIntent().getIntExtra("position", 0), false);
    }

    @Override
    public void onBackPressed() {
        // return current image index to MainActivity
        Intent returnIntent = new Intent();

        returnIntent.putExtra("position", mViewPager.getCurrentItem());
        returnIntent.putExtra("dirty", dirtyBit);

        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
