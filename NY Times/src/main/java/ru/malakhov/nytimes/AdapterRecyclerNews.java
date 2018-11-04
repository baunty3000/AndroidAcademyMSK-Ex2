package ru.malakhov.nytimes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

import ru.malakhov.nytimes.data.DataUtils;
import ru.malakhov.nytimes.data.NewsItem;

import androidx.annotation.NonNull;

public class AdapterRecyclerNews extends RecyclerView.Adapter<AdapterRecyclerNews.ViewHolder>{
    private List<NewsItem> mNewsItems;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public AdapterRecyclerNews(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setNewsItems(List<NewsItem> newsItems) {
        mNewsItems = newsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterRecyclerNews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_recycler_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerNews.ViewHolder holder, int position) {
        holder.bind(mNewsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsItems == null ? 0 : mNewsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitle;
        private final ImageView mImageUrl;
        private final TextView mCategory;
        private final TextView mPublishDate;
        private final TextView mPreviewText;

        public void bind(NewsItem newsItem) {
            mTitle.setText(newsItem.getTitle());
            Glide.with(mContext).load(newsItem.getImageUrl()).into(mImageUrl);
            mCategory.setText(newsItem.getCategory().getName());
            mPublishDate.setText(DataUtils.formatDateTime(mContext,newsItem.getPublishDate()));
            mPreviewText.setText(newsItem.getPreviewText());
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> ActivityFullNews.start(mContext, mNewsItems.get(getAdapterPosition())));
            mTitle = itemView.findViewById(R.id.news_title);
            mImageUrl = itemView.findViewById(R.id.news_image);
            mCategory = itemView.findViewById(R.id.news_category);
            mPublishDate = itemView.findViewById(R.id.news_date);
            mPreviewText = itemView.findViewById(R.id.news_previews_text);
        }
    }
}
