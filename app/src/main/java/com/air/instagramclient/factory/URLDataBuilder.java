package com.air.instagramclient.factory;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.air.instagramclient.Adaptors.InstagramPhotosAdaptor;
import com.air.instagramclient.helpers.ApplicationHelpers;
import com.air.instagramclient.models.InstagramPhotoModel;
import com.air.instagramclient.models.UserModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hetashah on 2/6/15.
 */
public class URLDataBuilder {
    public static final String CLIENT_ID = "159da63678444ca688af3466ef81bfab";
    private ArrayList<InstagramPhotoModel> photos;
    private InstagramPhotosAdaptor adaptor;
    private SwipeRefreshLayout swipeContainer;

    public URLDataBuilder(ArrayList<InstagramPhotoModel> photos, InstagramPhotosAdaptor adaptor, SwipeRefreshLayout swipeContainer) {
        this.photos = photos;
        this.adaptor = adaptor;
        this.swipeContainer = swipeContainer;
        if(ApplicationHelpers.isEmpty(this.photos)) {
            this.photos = new ArrayList<InstagramPhotoModel>();
        }
    }

    public void fetchPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //DO NOTHING
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(response != null) {
                    Log.i("DEBUG", "Response from API ==> " + response.toString());
                    try {
                        JSONArray listPosts = response.getJSONArray("data");
                        if(ApplicationHelpers.isNotEmpty(listPosts)) {
                            for (int i = 0; i < listPosts.length(); i++) {
                                JSONObject post = listPosts.getJSONObject(i);
                                if(post != null && ApplicationHelpers.isSupportType(post.getString("type"))) {
                                    InstagramPhotoModel model = new InstagramPhotoModel();
                                    model.setId(post.getString("id"));

                                    if(post.has("caption")) {
                                        JSONObject caption = post.getJSONObject("caption");

                                        if (caption != null) {
                                            model.setCaption(caption.getString("text"));
                                        }
                                    }

                                    if(post.has("images")) {
                                        JSONObject images = post.getJSONObject("images");
                                        if (images != null && images.has("standard_resolution")) {
                                            JSONObject imgURL = images.getJSONObject("standard_resolution");
                                            if (imgURL != null) {
                                                model.setImageURL(imgURL.getString("url"));
                                            }
                                        }
                                    }

                                    if(post.has("likes")) {
                                        JSONObject likes = post.getJSONObject("likes");
                                        if (likes != null) {
                                            model.setLikes(likes.getInt("count"));
                                        }
                                    }

                                    if(post.has("user")) {
                                        JSONObject user = post.getJSONObject("user");
                                        if (user != null) {
                                            UserModel userModel = new UserModel();
                                            userModel.setId(user.getString("id"));
                                            userModel.setName(user.getString("username"));
                                            userModel.setFullName(user.getString("full_name"));
                                            userModel.setProfileImgURL(user.getString("profile_picture"));
                                            model.setUser(userModel);
                                        }
                                    }

                                    photos.add(model);
                                }
                            }
                        }

                        Log.i("DEBUG", "Models ==> " + photos.toString());
                    } catch (JSONException e) {
                        Log.i("ERROR", "Exception occurred while converting JSON response ", e);
                    }

                    if(swipeContainer != null) {
                        swipeContainer.setRefreshing(false);
                    }

                    if(adaptor != null) {
                        adaptor.notifyDataSetChanged();
                    }

                } else {
                    Log.i("DEBUG", "No Response Received");
                }
            }
        });
    }
}
