package hugoyu.info.a500pxmobilechallenge.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.adapter.FullScreenAdapter;

/**
 * Created by Hugo on 2017-09-27.
 */

public class FullScreenActivity extends AppCompatActivity {

    ViewPager mViewPager;

    FullScreenAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        getSupportActionBar().hide();

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mAdapter = new FullScreenAdapter(this);
        mViewPager.setAdapter(mAdapter);

        // scroll to tapped image
        mViewPager.setCurrentItem(getIntent().getIntExtra("position", 0), false);
    }

    @Override
    public void onBackPressed() {
        // return current image index to MainActivity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", mViewPager.getCurrentItem());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
