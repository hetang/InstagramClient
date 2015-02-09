package com.air.instagramclient.Adaptors;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.air.instagramclient.R;

import java.util.ArrayList;

/**
 * Created by hetashah on 2/8/15.
 */
public class CommentsDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private ListView lvComments;
    private ArrayList<CharSequence> comments;

    public CommentsDialog() {
    }

    public void setComments(ArrayList<CharSequence> comments) {
        this.comments = comments;
    }

    public static CommentsDialog newInstance(String title, ArrayList<CharSequence> comments) {
        CommentsDialog frag = new CommentsDialog();
        frag.setComments(comments);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_dialog, container);
        lvComments = (ListView) view.findViewById(R.id.lvComments);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_list_item_1, comments);
        lvComments.setAdapter(adapter);
        lvComments.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
    }
}
