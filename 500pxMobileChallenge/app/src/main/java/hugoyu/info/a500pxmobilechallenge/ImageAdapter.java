package hugoyu.info.a500pxmobilechallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

/**
 * Created by Hugo on 2017-09-26.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    ArrayList<String> data;
    Context context;

    ImageLoader imageLoader;
    RequestQueue imgRequestQueue;

    public ImageAdapter(ArrayList<String> data, Context context) {
        this.data = data;
        this.context = context;

        imgRequestQueue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(imgRequestQueue,
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
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_image, parent, false);
        return new ImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        String imageUrl = data.get(position);
        holder.networkImageView.setImageUrl(imageUrl, imageLoader);
        ViewGroup.LayoutParams layoutParams = holder.networkImageView.getLayoutParams();
        if (layoutParams instanceof FlexboxLayoutManager.LayoutParams) {
            ((FlexboxLayoutManager.LayoutParams) layoutParams).setFlexGrow(1.0f);
        }
    }

    public void addData(ArrayList<String> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void replaceData(ArrayList<String> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView networkImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            networkImageView = (NetworkImageView) itemView;
        }
    }
}
