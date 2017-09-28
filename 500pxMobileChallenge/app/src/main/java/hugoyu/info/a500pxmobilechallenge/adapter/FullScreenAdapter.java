package hugoyu.info.a500pxmobilechallenge.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.model.MyGallery;
import hugoyu.info.a500pxmobilechallenge.model.MySharedImageLoader;

/**
 * Created by Hugo on 2017-09-28.
 */

public class FullScreenAdapter extends PagerAdapter {

    Context context;

    MySharedImageLoader imageLoader;

    public FullScreenAdapter(Context context) {
        this.context = context;

        imageLoader = MySharedImageLoader.getInstance(Volley.newRequestQueue(context),
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(100);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    @Override
    public int getCount() {
        return MyGallery.getData().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.item_full_screen_photo, container, false);
        NetworkImageView networkImageView = (NetworkImageView) v.findViewById(R.id.mNetworkImageView);

        String imageUrl = MyGallery.getData().get(position).getPhotoUrl();
        networkImageView.setImageUrl(imageUrl, imageLoader);

        container.addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
