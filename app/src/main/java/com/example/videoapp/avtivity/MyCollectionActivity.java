package com.example.videoapp.avtivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.example.videoapp.R;
import com.example.videoapp.adapter.CollectionAdapter;
import com.example.videoapp.adapter.VideoAdapter;
import com.example.videoapp.adapter.listener.OnItemChildClickListener;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.models.VideoListModel;
import com.example.videoapp.models.VideoModel;
import com.example.videoapp.util.StringUtils;
import com.example.videoapp.util.Tag;
import com.example.videoapp.util.Utils;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionActivity extends BaseActivity implements OnItemChildClickListener {

    private RecyclerView recyclerView;
    List<VideoModel> datas = new ArrayList<>();
    CollectionAdapter videoAdapter;

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

    @Override
    protected int initLayout() {
        return (R.layout.activity_my_collection);
    }

    @Override
    protected void initView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        this.recyclerView = mRecyclerView;

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
        mVideoView = new VideoView(this);
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
        mController = new StandardVideoController(this);
        mErrorView = new ErrorView(this);
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(this);
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(this);
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(this));
        mController.addControlComponent(new GestureView(this));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }





    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);

        CollectionAdapter videoAdapter = new CollectionAdapter(this);
        this.videoAdapter = videoAdapter;
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemChildClickListener(this);

        getVideoList();
    }



    private void getVideoList(){
        String token = getStringFromSp("token");
        if (!StringUtils.isEmpty(token)){

            MineRequest.getVideoList(this,token,1,5,0, new SeanCallBack() {
                @Override
                public void onSuccess(String response) {
//                   List<VideoModel> datas = new ArrayList<>();
//                   VideoAdapter videoAdapter = new VideoAdapter(getActivity(),datas);
//                   recyclerView.setAdapter(videoAdapter);

                    VideoListModel models = new Gson().fromJson(response,VideoListModel.class);
                    if (response!= null &&models.code == 0){
                        List<VideoModel> list = models.page.list;

                        if (list.size() > 0){
                            datas.addAll(list);
                            videoAdapter.setData(datas);
                            videoAdapter.notifyDataSetChanged();
                        }else {

                        }
                    }

                    Log.e("e",response);
                }

                @Override
                public void onFailure(Exception e) {

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
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }


    @Override
    public void onItemChildClick(int position) {
        startPlay(position);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}