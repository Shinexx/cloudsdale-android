package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.WazaBe.HoloEverywhere.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;

public class OnlineListFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        //TODO Do the real list of ponies :P
        LinearLayout layout = new LinearLayout(getActivity(), null);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(getActivity());
        tv.setText("I'm the list of online ponies!");
        tv.setGravity(Gravity.CENTER);
        TextView tv2 = new TextView(getActivity());
        tv2.setText("I'm a placeholder for NYI functionality");
        tv.setGravity(Gravity.CENTER);
        layout.addView(tv);
        layout.addView(tv2);

        return layout;
    }
    
}
