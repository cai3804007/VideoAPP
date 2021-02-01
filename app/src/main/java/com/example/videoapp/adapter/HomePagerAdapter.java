package com.example.videoapp.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class HomePagerAdapter extends MyPagerAdapter {

    public HomePagerAdapter(FragmentManager fm, String[] mTitles, ArrayList<Fragment> mFragments) {
        super(fm, mTitles, mFragments);
    }
}
