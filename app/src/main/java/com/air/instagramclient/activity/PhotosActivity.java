package com.air.instagramclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.air.instagramclient.Adaptors.CommentsDialog;
import com.air.instagramclient.Adaptors.InstagramPhotosAdaptor;
import com.air.instagramclient.R;
import com.air.instagramclient.factory.URLDataBuilder;
import com.air.instagramclient.models.InstagramPhotoModel;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    private InstagramPhotosAdaptor adaptor;
    private ArrayList<InstagramPhotoModel> photos;
    private SwipeRefreshLayout swipeContainer;
    private URLDataBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                photos.clear();
                builder.fetchPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        photos = new ArrayList<InstagramPhotoModel>();
        adaptor = new InstagramPhotosAdaptor(this, photos);

        ListView lstView = (ListView) findViewById(R.id.lvItems);
        lstView.setAdapter(adaptor);

        builder = new URLDataBuilder(this, photos, adaptor, swipeContainer);
        builder.fetchPhotos();

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {
                InstagramPhotoModel model = photos.get(position);
                builder.fetchComments(model.getId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialogComments(ArrayList<CharSequence> comments) {
        FragmentManager fm = getSupportFragmentManager();
        CommentsDialog alertDialog = CommentsDialog.newInstance("Comments", comments);
        alertDialog.show(fm, "dialog");
    }
}
