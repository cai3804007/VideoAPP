package com.example.videoapp.fragenmt;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.example.videoapp.R;
import com.example.videoapp.adapter.VideoAdapter;
import com.example.videoapp.adapter.listener.OnItemChildClickListener;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.avtivity.LoginActivity;
import com.example.videoapp.models.VideoListModel;
import com.example.videoapp.models.VideoModel;
import com.example.videoapp.util.StringUtils;
import com.example.videoapp.util.Tag;
import com.example.videoapp.util.Utils;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends BaseFragenmt implements OnItemChildClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String title;
    private RecyclerView recyclerView;

    private SmartRefreshLayout reshView;
    List<VideoModel> datas = new ArrayList<>();
    private Integer page = 1;
    private Integer pageSize = 5;
    public Integer type;
    VideoAdapter videoAdapter;



    protected LinearLayoutManager mLinearLayoutManager;
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;


    public VideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public VideoFragment newInstance(String title,Integer type) {
        VideoFragment fragment = new VideoFragment();
        fragment.title = title;
        fragment.type = type;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recyclerview);
        this.recyclerView = mRecyclerView;
        reshView = rootView.findViewById(R.id.reshView);

        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });

        initVideoView();

    }



    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * 开始播放
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoModel videoBean = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoBean.playurl);


//        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);;

        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }
    
    
    
    
    
    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);

        VideoAdapter videoAdapter = new VideoAdapter(getActivity());
        this.videoAdapter = videoAdapter;
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemChildClickListener(this);


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

        getVideoList();
    }




    private void footerRefresh(){
        page++;
        getVideoList();
    }

    private void headerRefresh(){
        page = 1;
        getVideoList();
    }


    private void getVideoList(){
       String token = getStringFromSp("token");
       if (!StringUtils.isEmpty(token)){
           MineRequest.getVideoList(getActivity(),token,page,pageSize,type, new SeanCallBack() {
               @Override
               public void onSuccess(String response) {
//                   List<VideoModel> datas = new ArrayList<>();
//                   VideoAdapter videoAdapter = new VideoAdapter(getActivity(),datas);
//                   recyclerView.setAdapter(videoAdapter);
                   if (page == 1){
                       reshView.finishRefresh();
                   }else {
                       reshView.finishLoadMore();
                   }
                   VideoListModel models = new Gson().fromJson(response,VideoListModel.class);
                    if (response!= null &&models.code == 0){
                        List<VideoModel> list = models.page.list;
                        if (page == 1){
                            datas.clear();
                        }
                        if (list.size() > 0){
                            datas.addAll(list);
                            videoAdapter.setData(datas);
                            videoAdapter.notifyDataSetChanged();
                        }else {
                            reshView.finishLoadMoreWithNoMoreData();
                        }
                    }

                   Log.e("e",response);
               }

               @Override
               public void onFailure(Exception e) {
                   reshView.finishRefresh();
                   reshView.finishLoadMore();
               }
           });
       }else {
           navigateTo(LoginActivity.class);
       }
    }


    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if(getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }


    @Override
    public void onItemChildClick(int position) {
        startPlay(position);

    }
}