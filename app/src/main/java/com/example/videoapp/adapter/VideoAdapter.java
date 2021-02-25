package com.example.videoapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dueeeke.videocontroller.component.PrepareView;
import com.example.videoapp.MyApplication;
import com.example.videoapp.R;
import com.example.videoapp.api.MineRequest;
import com.example.videoapp.api.SeanCallBack;
import com.example.videoapp.models.VideoModel;
import com.example.videoapp.util.CircleTransform;
import com.example.videoapp.util.GlideRoundTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.example.videoapp.adapter.listener.OnItemChildClickListener;
import com.example.videoapp.adapter.listener.OnItemClickListener;

import static android.widget.ImageView.ScaleType.FIT_XY;


public class VideoAdapter extends RecyclerView.Adapter {
    private Context context;

    public void setData(List<com.example.videoapp.models.VideoModel> data) {
        this.data = data;
    }

    private List<VideoModel> data;
    private Object VideoModel;

    public OnItemChildClickListener mOnItemChildClickListener;

    public OnItemClickListener mOnItemClickListener;

    public VideoAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        VideoModel model = data.get(position);
        vh.title.setText(model.vtitle);
        vh.subTitle.setText(model.author);
        vh.dzButton.setText(String.valueOf(model.likeNum));
        vh.collectButton.setText(String.valueOf(model.collectNum));
        vh.commentButton.setText(String.valueOf(model.commentNum));
        vh.model = model;
        if (!vh.collectionFlag) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.collect);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.collectButton.setCompoundDrawables(drawable, null, null, null);
            vh.collectButton.setTextColor(Color.parseColor("#EEE9E9"));

        } else {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.collect_select);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.collectButton.setCompoundDrawables(drawable, null, null, null);
            vh.collectButton.setTextColor(Color.parseColor("#E21918"));

        }

        if (!vh.likeFlag) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.dianzan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.dzButton.setCompoundDrawables(drawable, null, null, null);
            int b = context.getResources().getColor(R.color.gray81);//得到配置文件里的颜色
            vh.dzButton.setTextColor(b);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.dianzan_select);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.dzButton.setCompoundDrawables(drawable, null, null, null);
            int b = context.getResources().getColor(R.color.Red);//得到配置文件里的颜色
            vh.dzButton.setTextColor(b);
        }

        vh.mPosition = position;
        vh.userImage.setScaleType(FIT_XY);
        Picasso.with(this.context).load(model.headurl).placeholder(R.mipmap.user_nor).transform(new CircleTransform()).into(vh.userImage); //into(vh.userImage);
//        Picasso.with(this.context).load(model.coverurl).into(vh.imageView);

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round) //预加载图片
//                .error(R.drawable.ic_launcher_foreground) //加载失败图片
//                .priority(Priority.HIGH) //优先级
//                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
//                .transform(new GlideRoundTransform(5)); //圆角
//        Glide.with(context).load(list.get(position).getImage()).apply(options).into(holder.imageView);

//        Glide.with(this.context).load(model.headurl).transform((Transformation<Bitmap>) new CircleTransform()).into(vh.userImage);

        Glide.with(this.context).load(model.coverurl).into(vh.mThumb);
    }

    @Override
    public int getItemCount() {

        if (data == null) {
            return 0;
        }

        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView subTitle;
        //        private ImageView imageView;
        private Button dzButton;
        private Button collectButton;
        private Button commentButton;
        private ImageView userImage;

        public Boolean collectionFlag;
        public Boolean likeFlag;

        public int mPosition;
        public FrameLayout mPlayerContainer;
        public TextView mTitle;
        public ImageView mThumb;
        public PrepareView mPrepareView;

        public VideoModel model;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            subTitle = itemView.findViewById(R.id.subTile);
            userImage = itemView.findViewById(R.id.userImage);
            this.context = context;
            dzButton = itemView.findViewById(R.id.dzButton);
            collectButton = itemView.findViewById(R.id.collectButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            collectionFlag = false;
            likeFlag = false;
            collectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeNum = Integer.parseInt(collectButton.getText().toString());
                    if (collectionFlag) {
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.collect);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        collectButton.setCompoundDrawables(drawable, null, null, null);
                        collectButton.setText(String.valueOf(--likeNum));
                        collectButton.setTextColor(Color.parseColor("#EEE9E9"));
                    } else {
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.collect_select);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        collectButton.setCompoundDrawables(drawable, null, null, null);
                        collectButton.setText(String.valueOf(++likeNum));
                        collectButton.setTextColor(Color.parseColor("#E21918"));
                    }
                    collectionFlag = !collectionFlag;
//                    (int vid, int type, boolean flag)
                    updataCount(model.vid,1,collectionFlag);
                }

            });
            dzButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeNum = Integer.parseInt(dzButton.getText().toString());
                    if (likeFlag) {
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.dianzan);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        dzButton.setCompoundDrawables(drawable, null, null, null);
                        dzButton.setText(String.valueOf(--likeNum));
                        int b = context.getResources().getColor(R.color.gray81);//得到配置文件里的颜色
                        dzButton.setTextColor(b);
                    } else {
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.dianzan_select);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        dzButton.setCompoundDrawables(drawable, null, null, null);
                        dzButton.setText(String.valueOf(++likeNum));
                        int b = context.getResources().getColor(R.color.Red);//得到配置文件里的颜色
                        dzButton.setTextColor(b);
                    }
                    likeFlag = !likeFlag;
                    updataCount(model.vid,2,likeFlag);
                }
            });


            mPlayerContainer = itemView.findViewById(R.id.player_container);
            mPrepareView = itemView.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }

        }

        public void updataCount(int vid, int type, boolean flag) {
            MineRequest.upDateCount(vid, type, flag, new SeanCallBack() {
                @Override
                public void onSuccess(String response) {

                    Log.e("dianziananzanisnaisnas",response);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

        }


    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}


