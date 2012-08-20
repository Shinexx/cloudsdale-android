package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class OnlineListFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        //TODO Do the real list of ponies :P
        TextView tv = new TextView(getActivity());
        tv.setText("I'm a drop fragment!");

        return tv;
    }
    
}
