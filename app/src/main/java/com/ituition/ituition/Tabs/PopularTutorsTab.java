package com.ituition.ituition.Tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ituition.ituition.R;

/**
 * Created by mushfiq on 12/14/17.
 */

public class PopularTutorsTab extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popular_tutors_tab, container, false);
    }
}
