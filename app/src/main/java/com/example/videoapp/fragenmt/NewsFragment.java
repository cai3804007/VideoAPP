package com.example.videoapp.fragenmt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.JsonUtils;
import com.example.videoapp.R;
import com.example.videoapp.adapter.NewsAdapter;
import com.example.videoapp.adapter.VideoAdapter;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.models.HomeTitleModel;
import com.example.videoapp.models.NewsModel;
import com.example.videoapp.models.VideoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragenmt {
    private RecyclerView recyclerView;
    protected LinearLayoutManager mLinearLayoutManager;
    private SmartRefreshLayout reshView;
    List<NewsModel> datas = new ArrayList<>();
    private Integer page = 1;
    private Integer pageSize = 5;
    NewsAdapter videoAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }



    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview);
        this.recyclerView = mRecyclerView;
        reshView = rootView.findViewById(R.id.reshView);
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);

        NewsAdapter videoAdapter = new NewsAdapter(getActivity());
        this.videoAdapter = videoAdapter;

        recyclerView.setAdapter(videoAdapter);


       loadData();
        reshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                headerRefresh();
            }
        });

        reshView.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                footerRefresh();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }


    private void footerRefresh(){
        page++;
        loadData();
    }

    private void headerRefresh(){
        page = 1;
        loadData();
    }

    public  void loadData(){
        MineRequest.getNewList(getActivity(), getStringFromSp("token"), page, pageSize, new SeanCallBack() {
            @Override
            public void onSuccess(String response) {

                JSONObject object = JsonUtils.getJSONObject(response,"page",new JSONObject());
                JSONArray array = JsonUtils.getJSONArray(object,"list",new JSONArray());

                List<NewsModel> models = new Gson().fromJson(array.toString(),new TypeToken<List<NewsModel>>(){}.getType());

                for (int i = 0; i < models.size(); i++) {
                    NewsModel model = models.get(i);
                    datas.add(model);
                }
                videoAdapter.setData(datas);
                videoAdapter.notifyDataSetChanged();
                reshView.finishRefresh();
                reshView.finishLoadMore();
            }

            @Override
            public void onFailure(Exception e) {
                reshView.finishRefresh();
                reshView.finishLoadMore();
            }
        });

    }

}