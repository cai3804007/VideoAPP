package com.example.videoapp.fragenmt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.JsonUtils;
import com.example.videoapp.R;
import com.example.videoapp.adapter.HomePagerAdapter;
import com.example.videoapp.adapter.MyPagerAdapter;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.models.HomeTitleModel;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragenmt {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SlidingTabLayout titleView;
    ViewPager homePager;
    private  String[] mTitles;
    private MyPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        titleView = rootView.findViewById(R.id.home_list);
        homePager = rootView.findViewById(R.id.home_page);
    }

    @Override
    protected void initData() {

        MineRequest.getTitleList(this.getActivity(), getStringFromSp("token"), new SeanCallBack() {
            @Override
            public void onSuccess(String response) {
                JSONObject object = JsonUtils.getJSONObject(response,"page",new JSONObject());

                JSONArray array = JsonUtils.getJSONArray(object,"list",new JSONArray());

                List<HomeTitleModel> models = new Gson().fromJson(array.toString(),new TypeToken<List<HomeTitleModel>>(){}.getType());
                mTitles = new String[models.size()];
                for (int i = 0; i < models.size(); i++) {
                    HomeTitleModel model = models.get(i);
                    mTitles[i] = model.categoryName;
                    mFragments.add(new VideoFragment().newInstance(model.categoryName,model.categoryId));
                }

                homePager.setOffscreenPageLimit(mFragments.size());
                mAdapter = new HomePagerAdapter(getFragmentManager(),mTitles,mFragments);

                homePager.setAdapter(mAdapter);
                titleView.setViewPager(homePager,mTitles);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });


    }

}