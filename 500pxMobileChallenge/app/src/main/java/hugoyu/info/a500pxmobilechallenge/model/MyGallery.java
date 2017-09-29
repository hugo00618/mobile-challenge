package hugoyu.info.a500pxmobilechallenge.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hugoyu.info.a500pxmobilechallenge.R;
import hugoyu.info.a500pxmobilechallenge.adapter.DataChangeNotifiable;

/**
 * Created by Hugo on 2017-09-28.
 */

public class MyGallery {

    static ArrayList<MyPhoto> data = new ArrayList<>();
    boolean loading = false;
    int nextPage = 1;

    Context context;
    RequestQueue mRequestQueue;

    ArrayList<DataChangeNotifiable> notifyingAdapters = new ArrayList<>();

//    public static Bitmap sharedBitmap;

    private static MyGallery instance;

    private MyGallery(Context context, RequestQueue mRequestQueue) {
        this.context = context;
        this.mRequestQueue = mRequestQueue;
    }

    public static MyGallery getInstance(Context context, RequestQueue mRequestQueue) {
        if (instance == null) {
            instance = new MyGallery(context, mRequestQueue);
        }
        return instance;
    }

    public static ArrayList<MyPhoto> getData() {
        return data;
    }

    public void addData(ArrayList<MyPhoto> newData) {
        data.addAll(newData);
        notifyDataChange();
    }

    public void replaceData(ArrayList<MyPhoto> newData) {
        data = newData;
        notifyDataChange();
    }

    public void notifyDataChange() {
        for (DataChangeNotifiable adapter: notifyingAdapters) {
            adapter.notifyDataSetChanged();
        }
        notifyingAdapters = new ArrayList<>();
    }

    public void loadNextPage(final boolean refresh, final DataChangeNotifiable adapter,
                             @Nullable final SwipeRefreshLayout mSwipeRefreshLayout) {
        // add adapter to notify list
        notifyingAdapters.add(adapter);

        if (loading) {
            return;
        }

        // reset nextPage to 1 if it's refreshing gallery
        if (refresh) {
            nextPage = 1;
        }

        String url = context.getString(R.string.api_url_photos);
        url += "?feature=popular";
        url += "&image_size=4";
        url += "&consumer_key=" + context.getString(R.string.api_key);
        url += "&page=" + nextPage;

        Log.d("Page Loading", String.valueOf(nextPage));
        loading = true;
        mRequestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Page Loaded", String.valueOf(nextPage));

                            JSONArray jsonArray = response.getJSONArray("photos");
                            ArrayList<MyPhoto> newData = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject myPhotoObj = jsonArray.getJSONObject(i);
                                if (myPhotoObj.getInt("category") != 4) { // if photo is not nude
                                    String iso = myPhotoObj.getString("iso");
                                    String shutterSpeed = myPhotoObj.getString("shutter_speed");
                                    String aperture = myPhotoObj.getString("aperture");
                                    String photoUrl = myPhotoObj.getJSONArray("images").getJSONObject(0).getString("url");
                                    String userDisplayName = myPhotoObj.getJSONObject("user").getString("fullname");
                                    String userPicUrl = myPhotoObj.getJSONObject("user").getString("userpic_url");
                                    int width = myPhotoObj.getInt("width");
                                    int height = myPhotoObj.getInt("height");
                                    MyPhoto newPhoto = new MyPhoto(iso, shutterSpeed, aperture,
                                            photoUrl, userDisplayName, userPicUrl, width, height);

                                    newData.add(newPhoto);
                                }
                            }

                            if (refresh) { // if refresh, replace data
                                replaceData(newData);
                            } else { // otherwise concatenate new data
                                addData(newData);
                            }

                            loading = false;
                            // stop refresh animation if there is any
                            if (mSwipeRefreshLayout != null) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            nextPage++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null));
    }
}
