package com.codepath.newssearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.newssearch.R;
import com.codepath.newssearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sharath on 7/26/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, ArrayList<Article> articles){
        super(context, R.layout.item_article,articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for current position.
        Article article = getItem(position);
        ArticleViewHolder articleViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article,parent,false);
            articleViewHolder = new ArticleViewHolder();
            articleViewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            articleViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(articleViewHolder);
        }else{
            articleViewHolder = (ArticleViewHolder) convertView.getTag();
        }

        articleViewHolder.tvTitle.setText(article.getHeadline());
        articleViewHolder.ivImage.setImageResource(0);
        if(TextUtils.isEmpty(article.getThumbNail())){
            /*Picasso.with(getContext())
                    .load(article.getThumbNail())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(articleViewHolder.ivImage);*/
        }else{
            Glide.with(getContext())
                    .load(article.getThumbNail())
                    .override(75,75)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(articleViewHolder.ivImage);
        }

        return convertView;
    }

    public static class ArticleViewHolder{
        ImageView ivImage;
        TextView tvTitle;
    }
}
