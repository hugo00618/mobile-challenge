package hugoyu.info.a500pxmobilechallenge.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.model.MyGallery;
import hugoyu.info.a500pxmobilechallenge.model.MyPhoto;
import hugoyu.info.a500pxmobilechallenge.model.MySharedImageLoader;

/**
 * Created by Hugo on 2017-09-28.
 */

public class FullScreenAdapter extends PagerAdapter implements DataChangeNotifiable {

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

        MyPhoto myPhoto = MyGallery.getData().get(position);

        // photo
        NetworkImageView networkImgPhoto = (NetworkImageView) v.findViewById(R.id.mNetworkImageView);
        networkImgPhoto.setImageUrl(myPhoto.getPhotoUrl(), imageLoader);

        // user profile pic
        NetworkImageView networkImgProfilePic = (NetworkImageView) v.findViewById(R.id.networkImgProfilePic);
        networkImgProfilePic.setImageUrl(myPhoto.getUserPicUrl(), imageLoader);

        // user display name
        TextView textFullName = (TextView) v.findViewById(R.id.textFullName);
        textFullName.setText(myPhoto.getUserDisplayName());

        // shutter speed
        TextView textSpeed = (TextView) v.findViewById(R.id.textSpeed);
        String shutterSpeed = myPhoto.getShutterSpeed();
        if (shutterSpeed.equals("null") || shutterSpeed.equals("")) {
            textSpeed.setText("N/A");
        } else {
            textSpeed.setText(shutterSpeed + "s");
        }

        // aperture
        TextView textAperture = (TextView) v.findViewById(R.id.textAperture);
        String aperture = myPhoto.getAperture();
        if (aperture.equals("null") || aperture.equals("")) {
            textAperture.setText("N/A");
        } else {
            // f/8 just looks awkward... append ".0" at the end if it's a int
            if (!aperture.contains(".")) {
                aperture += ".0";
            }

            textAperture.setText("f/" + aperture);
        }

        // iso
        TextView textIso = (TextView) v.findViewById(R.id.textIso);
        String iso = myPhoto.getIso();
        if (iso.equals("null") || iso.equals("")) {
            textIso.setText("N/A");
        } else {
            textIso.setText(iso);
        }

        container.addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
