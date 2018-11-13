
package ru.malakhov.nytimes.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.network.dto.ResultDTO;

public class AdapterRecyclerNews extends RecyclerView.Adapter<ViewHolderNews>{
    private List<ResultDTO> mNewsItems;
    private final Activity mActivity;
    private final LayoutInflater mInflater;

    public AdapterRecyclerNews(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public void setNewsItems(List<ResultDTO> newsItems) {
        mNewsItems = newsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderNews onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        return new ViewHolderNews(mInflater.inflate(R.layout.item_recycler_news, parent, false), mActivity, mNewsItems);
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
