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

import ru.malakhov.nytimes.data.NewsItem;

import androidx.annotation.NonNull;

public class AdapterRecyclerNews extends RecyclerView.Adapter<AdapterRecyclerNews.ViewHolder>{
    private final List<NewsItem> mNewsItems;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public AdapterRecyclerNews(Context context, List<NewsItem> news) {
        mNewsItems = news;
        mContext = context;
        mInflater = LayoutInflater.from(context);
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
        return mNewsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView imageUrl;
        private final TextView category;
        private final TextView publishDate;
        private final TextView previewText;

        public void bind(NewsItem newsItem) {
            title.setText(newsItem.getTitle());
            Glide.with(mContext).load(newsItem.getImageUrl()).into(imageUrl);
            category.setText(newsItem.getCategory().getName());
            publishDate.setText(newsItem.getPublishDate().toString());
            previewText.setText(newsItem.getPreviewText());
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityFullNews.start(mContext, mNewsItems.get(getAdapterPosition()));
                }
            });
            title = itemView.findViewById(R.id.tvNewsTitle);
            imageUrl = itemView.findViewById(R.id.ivNewsImage);
            category = itemView.findViewById(R.id.tvNewsCategory);
            publishDate = itemView.findViewById(R.id.tvNewsDate);
            previewText = itemView.findViewById(R.id.tvNewsPreviewText);
        }
    }
}
