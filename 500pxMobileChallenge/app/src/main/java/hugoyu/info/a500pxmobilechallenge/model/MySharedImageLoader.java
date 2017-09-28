package hugoyu.info.a500pxmobilechallenge.model;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Hugo on 2017-09-28.
 */

public class MySharedImageLoader extends ImageLoader {
    private static MySharedImageLoader instance;

    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    private MySharedImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }

    public static MySharedImageLoader getInstance(RequestQueue queue, ImageCache imageCache) {
        if (instance == null) {
            instance = new MySharedImageLoader(queue, imageCache);
        }
        return instance;
    }
}
