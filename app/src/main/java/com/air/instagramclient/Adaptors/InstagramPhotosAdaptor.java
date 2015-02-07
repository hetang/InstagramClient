package com.air.instagramclient.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.instagramclient.R;
import com.air.instagramclient.models.InstagramPhotoModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by hetashah on 2/7/15.
 */
public class InstagramPhotosAdaptor extends ArrayAdapter<InstagramPhotoModel>{

    private static class ViewHolder{
        ImageView imgVwPhoto;
        TextView txtVwCaption;
    }

    public InstagramPhotosAdaptor(Context context, List<InstagramPhotoModel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhotoModel model = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            // Do not attach to parent yet. Hence, we are passing false here
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,
                                                                    parent, false);

            viewHolder.imgVwPhoto = (ImageView) convertView.findViewById(R.id.imgVwPhoto);
            viewHolder.txtVwCaption = (TextView) convertView.findViewById(R.id.txtVwCaption);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtVwCaption.setText(model.getCaption());
        viewHolder.imgVwPhoto.setImageResource(0);
        Picasso.with(getContext()).load(model.getImageURL()).fit().centerCrop().placeholder(R.drawable.loading1).into(viewHolder.imgVwPhoto);

        return convertView;
    }
}
