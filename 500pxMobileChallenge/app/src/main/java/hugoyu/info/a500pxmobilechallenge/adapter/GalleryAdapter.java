package hugoyu.info.a500pxmobilechallenge.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayoutManager;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.activity.FullScreenActivity;
import hugoyu.info.a500pxmobilechallenge.model.MyGallery;
import hugoyu.info.a500pxmobilechallenge.model.MySharedImageLoader;

/**
 * Created by Hugo on 2017-09-26.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    Context context;

    MySharedImageLoader imageLoader;
    RequestQueue imgRequestQueue;

    public GalleryAdapter(Context context) {
        this.context = context;

        imgRequestQueue = Volley.newRequestQueue(context);

        imageLoader = MySharedImageLoader.getInstance(imgRequestQueue,
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
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = inflater.inflate(R.layout.item_thumbnail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return MyGallery.getData().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView networkImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            networkImageView = (NetworkImageView) itemView;
        }

        public void bindView(int position) {
            String imageUrl = MyGallery.getData().get(position).getPhotoUrl();
            networkImageView.setImageUrl(imageUrl, imageLoader);

            ViewGroup.LayoutParams layoutParams = networkImageView.getLayoutParams();
            if (layoutParams instanceof FlexboxLayoutManager.LayoutParams) {
                ((FlexboxLayoutManager.LayoutParams) layoutParams).setFlexGrow(1.0f);
                ((FlexboxLayoutManager.LayoutParams) layoutParams).setAlignSelf(AlignSelf.STRETCH);
            }

            networkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullScreenActivity.class);

                    intent.putExtra("position", getLayoutPosition());

//                    Bitmap bmp = ((BitmapDrawable) networkImageView.getDrawable()).getBitmap();
//                    MyGallery.sharedBitmap = bmp;

//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation((Activity) context,
//                                    networkImageView,
//                                    ViewCompat.getTransitionName(networkImageView));

//                    context.startActivity(intent, options.toBundle());

                    ((Activity) context).startActivityForResult(intent, 1);
                }
            });
        }
    }
}
