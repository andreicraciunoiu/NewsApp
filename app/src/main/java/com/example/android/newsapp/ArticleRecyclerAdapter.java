package com.example.android.newsapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {

    private final List<Article> articleList;
    private final MainActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView section;
        final TextView title;

        ViewHolder(View view) {
            super(view);
            section = (TextView) view.findViewById(R.id.section_id);
            title = (TextView) view.findViewById(R.id.title_id);
        }
    }

    ArticleRecyclerAdapter(MainActivity context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public ArticleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ArticleRecyclerAdapter.ViewHolder holder, int position) {
        final Article currentArticle = articleList.get(position);
        holder.section.setText(currentArticle.getSection());
        holder.title.setText(currentArticle.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startWebView(currentArticle.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}