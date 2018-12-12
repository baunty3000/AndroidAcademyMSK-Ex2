
package ru.malakhov.nytimes.ui.fragments.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.room.NewsEntity;

public class AdapterRecyclerNews extends RecyclerView.Adapter<ViewHolderNews>{
    private List<NewsEntity> mNewsItems = new ArrayList<>();
    private final Context mContext;
    private final LayoutInflater mInflater;

    public AdapterRecyclerNews(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setNewsItems(List<NewsEntity> newsItems) {
        mNewsItems.clear();
        mNewsItems.addAll(newsItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderNews onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        return new ViewHolderNews(mInflater.inflate(R.layout.item_recycler_news, parent, false), mContext, mNewsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNews holder, int position) {
        holder.bind(mNewsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsItems == null ? 0 : mNewsItems.size();
    }
}
