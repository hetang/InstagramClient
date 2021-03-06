package com.air.instagramclient.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.air.instagramclient.R;
import com.air.instagramclient.helpers.RoundedTransform;
import com.air.instagramclient.models.InstagramPhotoModel;
import com.air.instagramclient.models.UserModel;
import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by hetashah on 2/7/15.
 */
public class InstagramPhotosAdaptor extends ArrayAdapter<InstagramPhotoModel>{

    private static class ViewHolder{
        ImageView imgVwPhoto;
        ImageView imgVwProfile;
        TextView txtVwCaption;
        TextView txtVwLikes;
        TextView txtVwUserName;
        TextView txtVwTime;
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
            Typeface fontRingm = Typeface.createFromAsset(getContext().getAssets(), "fonts/RINGM.ttf");

            Drawable dr = getContext().getResources().getDrawable(R.drawable.heart1);
            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
            // Scale it to 50 x 50
            Drawable d = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmap, 45, 45, true));
            int h = d.getIntrinsicHeight();
            int w = d.getIntrinsicWidth();
            d.setBounds( 0, 0, w, h );

            // Do not attach to parent yet. Hence, we are passing false here
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,
                                                                    parent, false);

            viewHolder.imgVwPhoto = (ImageView) convertView.findViewById(R.id.imgVwPhoto);
            viewHolder.imgVwProfile = (ImageView) convertView.findViewById(R.id.imgVwProfile);
            viewHolder.txtVwCaption = (TextView) convertView.findViewById(R.id.txtVwCaption);
            viewHolder.txtVwCaption.setTypeface(fontRingm);
            viewHolder.txtVwLikes = (TextView) convertView.findViewById(R.id.txtVwLikes);
            viewHolder.txtVwLikes.setTypeface(fontRingm);
            viewHolder.txtVwLikes.setCompoundDrawables(d, null, null, null);
            viewHolder.txtVwLikes.setCompoundDrawablePadding(10);
            viewHolder.txtVwUserName = (TextView) convertView.findViewById(R.id.txtVwUserName);
            viewHolder.txtVwTime = (TextView) convertView.findViewById(R.id.txtVwTime);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StringBuilder userName = new StringBuilder();
        UserModel user = model.getUser();
        if(user != null) {
            userName.append("<b>").append(user.getName()).append("</b> ");
            viewHolder.txtVwUserName.setText(user.getName());

            Picasso.with(getContext())
                    .load(user.getProfileImgURL())
                    .fit()
                    .transform(new RoundedTransform())
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(viewHolder.imgVwProfile);
        }
        userName.append(model.getCaption());

        viewHolder.txtVwCaption.setText(Html.fromHtml(userName.toString()));
        viewHolder.txtVwLikes.setText(Html.fromHtml("<b>" + model.getLikes() + "</b> " + getContext().getResources().getString(R.string.likes_string)));
        viewHolder.txtVwTime.setText(model.getTime());
        viewHolder.imgVwPhoto.setImageResource(0);

        Picasso.with(getContext()).load(model.getImageURL()).fit().centerCrop().placeholder(R.drawable.loading1).into(viewHolder.imgVwPhoto);

        return convertView;
    }
}
