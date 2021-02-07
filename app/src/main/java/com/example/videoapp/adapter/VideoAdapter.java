package com.example.videoapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.example.videoapp.R;
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
    private  Context context;

    public void setData(List<com.example.videoapp.models.VideoModel> data) {
        this.data = data;
    }

    private List<VideoModel> data;
    private Object VideoModel;

    public OnItemChildClickListener mOnItemChildClickListener;

    public OnItemClickListener mOnItemClickListener;
    public  VideoAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         ViewHolder vh = (ViewHolder )holder;
         VideoModel model = data.get(position);
         vh.title.setText(model.vtitle);
         vh.subTitle.setText(model.author);
         vh.dzButton.setText(String.valueOf(model.likeNum));
         vh.collectButton.setText(String.valueOf(model.collectNum));
         vh.commentButton.setText(String.valueOf(model.commentNum));
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

        if (data == null){
            return 0;
        }

        return data.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView subTitle;
//        private ImageView imageView;
        private Button dzButton;
        private Button collectButton;
        private Button commentButton;
        private ImageView userImage;


        public int mPosition;
        public FrameLayout mPlayerContainer;
        public TextView mTitle;
        public ImageView mThumb;
        public PrepareView mPrepareView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            subTitle = itemView.findViewById(R.id.subTile);
            userImage = itemView.findViewById(R.id.userImage);

            dzButton = itemView.findViewById(R.id.dzButton);
            collectButton = itemView.findViewById(R.id.collectButton);
            commentButton = itemView.findViewById(R.id.commentButton);

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

        }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}


