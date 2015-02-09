package com.air.instagramclient.factory;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;

import com.air.instagramclient.Adaptors.CommentsDialog;
import com.air.instagramclient.Adaptors.InstagramPhotosAdaptor;
import com.air.instagramclient.activity.PhotosActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hetashah on 2/6/15.
 */
public class URLDataBuilder {
    public static final String CLIENT_ID = "159da63678444ca688af3466ef81bfab";
    private ArrayList<InstagramPhotoModel> photos;
    private InstagramPhotosAdaptor adaptor;
    private SwipeRefreshLayout swipeContainer;
    private PhotosActivity photosActivity;

    public URLDataBuilder(PhotosActivity photosActivity, ArrayList<InstagramPhotoModel> photos, InstagramPhotosAdaptor adaptor, SwipeRefreshLayout swipeContainer) {
        this.photosActivity = photosActivity;
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
                                Log.i("DEBUG", " i = " + i + " post = " + post);
                                if(post != null && ApplicationHelpers.isSupportType(post.getString("type"))) {
                                    InstagramPhotoModel model = new InstagramPhotoModel();
                                    model.setId(post.getString("id"));

                                    if(post.has("caption") && !post.isNull("caption")) {
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
                                    if(post.has("created_time")) {
                                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        long currentMillisecond = cal.getTimeInMillis();
                                        long postedMillisecond = currentMillisecond;
                                        try{
                                            String time = post.getString("created_time");
                                            if(!ApplicationHelpers.isEmpty(time)) {
                                                postedMillisecond  = Long.parseLong(time);
                                            }
                                        } catch(Exception e) {

                                        }

                                        long diff = currentMillisecond - postedMillisecond;
                                        int seconds = (int) (diff / 1000) % 60 ;
                                        int minutes = (int) ((diff / (1000*60)) % 60);
                                        int hours   = (int) ((diff / (1000*60*60)) % 24);
                                        StringBuilder time = new StringBuilder();
                                        if(hours > 0) {
                                            time.append(hours).append("h").append(" ago");
                                        } else if(minutes > 0) {
                                            time.append(hours).append("m").append(" ago");
                                        } else if(seconds > 0) {
                                            time.append(hours).append("s").append(" ago");
                                        }

                                        model.setTime(time.toString());
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

    public void fetchComments(String mediaId) {
        String url = "https://api.instagram.com/v1/media/" + mediaId + "?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //DO NOTHING
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(response != null) {
                    Log.i("DEBUG", "Response from API ==> " + response.toString());
                    try {
                        ArrayList<CharSequence> comments = new ArrayList<CharSequence>();
                        JSONObject data = response.getJSONObject("data");
                        JSONObject commentsData = data.getJSONObject("comments");
                        JSONArray commentsList = commentsData.getJSONArray("data");
                        if(ApplicationHelpers.isNotEmpty(commentsList)) {
                            for (int i = 0; i < commentsList.length(); i++) {
                                JSONObject commentData = commentsList.getJSONObject(i);
                                JSONObject commentFrom = commentData.getJSONObject("from");
                                StringBuilder comment = new StringBuilder();
                                comment.append("<b>").append(commentFrom.getString("username")).append("</b> ")
                                        .append(commentData.getString("text"));
                                comments.add(Html.fromHtml(comment.toString()));
                            }
                            Log.i("DEBUG", "Comments Models ==> " + comments.toString());
                            photosActivity.showDialogComments(comments);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
