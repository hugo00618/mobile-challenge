package hugoyu.info.a500pxmobilechallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by Hugo on 2017-09-27.
 */

public class FullScreenActivity extends AppCompatActivity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        mImageView = (ImageView) findViewById(R.id.mImageView);
        mImageView.setImageBitmap(MySharedBitmap.bmp);
    }
}
