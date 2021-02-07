package com.example.videoapp.adapter;

import android.content.Context;
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
import com.dueeeke.videocontroller.component.PrepareView;
import com.example.videoapp.R;
import com.example.videoapp.adapter.listener.OnItemChildClickListener;
import com.example.videoapp.adapter.listener.OnItemClickListener;
import com.example.videoapp.models.NewsModel;
import com.example.videoapp.models.VideoModel;
import com.example.videoapp.util.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.ImageView.ScaleType.FIT_XY;

public class NewsAdapter extends RecyclerView.Adapter{
    private Context context;

    public void setData(List<com.example.videoapp.models.NewsModel> data) {

        this.data = data;
    }

    private List<NewsModel> data;

    public OnItemChildClickListener mOnItemChildClickListener;

    public OnItemClickListener mOnItemClickListener;
    public  NewsAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder hoder;
        switch (viewType){
            case 1:{
                View view = LayoutInflater.from(context).inflate(R.layout.news_one_item,parent,false);
                NewsAdapter.NewsViewOneHolder viewHolder = new NewsAdapter.NewsViewOneHolder(view);
                hoder = viewHolder;
            }break;
            case 2:{
                View view = LayoutInflater.from(context).inflate(R.layout.news_three_item,parent,false);
                NewsAdapter.NewsViewThreeHolder viewHolder = new NewsAdapter.NewsViewThreeHolder(view);
                hoder = viewHolder;

            }break;
            default:{
                View view = LayoutInflater.from(context).inflate(R.layout.news_two_item,parent,false);
                NewsAdapter.NewsViewTwoHolder viewHolder = new NewsAdapter.NewsViewTwoHolder(view);
                hoder = viewHolder;
            }
                break;
        }
        return hoder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);
        NewsModel model = data.get(position);
        List<NewsModel.ThumbEntitiesBean> imageList = model.thumbEntities;

        String bttom = model.authorName + "." + model.commentCount.toString() + "评论" + "." + model.releaseDate;

                switch (type){
            case 1:{
                NewsViewOneHolder oneHolder = (NewsViewOneHolder) holder;
                oneHolder.title.setText(model.newsTitle);
                Picasso.with(this.context).load(model.headerUrl).placeholder(R.mipmap.user_nor)
                        .transform(new CircleTransform()).into(oneHolder.userImage); //into(vh.userImage);

                Glide.with(this.context).load(imageList.get(0).thumbUrl).into(oneHolder.imageView);
                oneHolder.timeView.setText(bttom);
            }break;
            case 2:{
                NewsViewThreeHolder oneHolder = (NewsViewThreeHolder) holder;
                oneHolder.title.setText(model.newsTitle);
                Picasso.with(this.context).load(model.headerUrl).placeholder(R.mipmap.user_nor)
                        .transform(new CircleTransform()).into(oneHolder.userImage); //into(vh.userImage);
                List<ImageView> images = new ArrayList<>();
                images.add(oneHolder.oneimageView);
                images.add(oneHolder.twoimageView);
                images.add(oneHolder.threeimageView);

                for (int i = 0; i < imageList.size(); i++) {
                    ImageView imag = images.get(i);
                    Glide.with(this.context).load(imageList.get(i).thumbUrl).into(imag);
                }
                oneHolder.timeView.setText(bttom);
            }break;
            default:{
                NewsViewTwoHolder oneHolder = (NewsViewTwoHolder) holder;
                oneHolder.title.setText(model.newsTitle);
                Picasso.with(this.context).load(model.headerUrl).placeholder(R.mipmap.user_nor)
                        .transform(new CircleTransform()).into(oneHolder.userImage); //into(vh.userImage);

                Glide.with(this.context).load(imageList.get(0).thumbUrl).into(oneHolder.imageView);
                oneHolder.timeView.setText(bttom);
            }
            break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        NewsModel model = data.get(position);
        return model.type;
    }

    @Override
    public int getItemCount() {

        if (data == null){
            return 0;
        }

        return data.size();
    }

    public  class  NewsViewOneHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView imageView;

        private ImageView userImage;
        private TextView timeView;
        public NewsViewOneHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.oneImage);
            userImage = itemView.findViewById(R.id.userimage);
            timeView = itemView.findViewById(R.id.timetext);


        }
    }

    public  class  NewsViewTwoHolder extends RecyclerView.ViewHolder {



        private TextView title;
        private ImageView imageView;

        private ImageView userImage;
        private TextView timeView;
        public NewsViewTwoHolder(@NonNull View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.oneImage);
            userImage = itemView.findViewById(R.id.userimage);
            timeView = itemView.findViewById(R.id.timetext);

        }
    }

    public  class  NewsViewThreeHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView oneimageView;
        private ImageView twoimageView;
        private ImageView threeimageView;
        private ImageView userImage;
        private TextView timeView;

        public NewsViewThreeHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            oneimageView = itemView.findViewById(R.id.oneImage);
            twoimageView = itemView.findViewById(R.id.twoImage);
            threeimageView = itemView.findViewById(R.id.threeImage);
            userImage = itemView.findViewById(R.id.userimage);
            timeView = itemView.findViewById(R.id.timetext);

        }
    }

}
