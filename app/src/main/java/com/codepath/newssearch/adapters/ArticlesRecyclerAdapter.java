package com.codepath.newssearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.newssearch.R;
import com.codepath.newssearch.models.Article;
import com.codepath.newssearch.util.Constants;

import java.util.List;

/**
 * Created by Sharath on 7/31/16.
 */
public class ArticlesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Article> mArticles;

    private static final int VIEW_TYPE_WITH_IMAGE = 0;
    private static final int VIEW_TYPE_WITH_OUT_IMAGE = 1;

    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ArticlesRecyclerAdapter(Context context, List<Article> articles){
        mArticles = articles;
        mContext = context;
    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if(mArticles.get(position).getMultimedia() == null || mArticles.get(position).getMultimedia().isEmpty()){
            return VIEW_TYPE_WITH_OUT_IMAGE;
        }
        return VIEW_TYPE_WITH_IMAGE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        // Inflate the custom layout
        if(viewType == VIEW_TYPE_WITH_IMAGE){
            View articleView = inflater.inflate(R.layout.item_article_rv, parent, false);
            viewHolder = new ImageViewHolder(articleView);
        }else{
            View articleWithoutImageView = inflater.inflate(R.layout.item_article_rv_onlytext, parent, false);
            viewHolder = new ViewHolder(articleWithoutImageView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        if(holder.getItemViewType() == VIEW_TYPE_WITH_IMAGE){
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.tvTitle.setText(article.getHeadline().getMain());
            imageViewHolder.ivImage.setImageResource(0);
            if(article.getMultimedia() != null && !article.getMultimedia().isEmpty()){
                Glide.with(getContext())
                        .load(Constants.NY_TIMES_BASE_URL+article.getMultimedia().get(0).getUrl())
                        //.override(75,75)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageViewHolder.ivImage);
            }
        }else{
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvTitle.setText(article.getHeadline().getMain());
        }

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvTitle;

        public ImageViewHolder(final View itemView){
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView,getLayoutPosition());
                }
            });
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;

        public ViewHolder(final View itemView){
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView,getLayoutPosition());
                }
            });
        }

    }
}
