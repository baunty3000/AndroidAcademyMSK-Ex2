
package ru.malakhov.nytimes.ui.fragments.news.adapter;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import ru.malakhov.nytimes.R;
import ru.malakhov.nytimes.data.room.NewsEntity;
import ru.malakhov.nytimes.ui.fragments.news.MessageFragmentListener;

public class ViewHolderNews extends RecyclerView.ViewHolder {

    public final static String KEY_HOLDER = "KEY_HOLDER";
    private TextView mTitle;
    private ImageView mImageUrl;
    private TextView mCategory;
    private TextView mPublishDate;
    private TextView mPreviewText;
    private ProgressBar mProgressBar;
    private Context mContext;

    private MessageFragmentListener mListener;

    public ViewHolderNews(@NonNull View itemView, List<NewsEntity> newsItems, Context context) {
        super(itemView);
        findViews(itemView);
        mContext = context;

        if (context instanceof MessageFragmentListener){
            mListener = (MessageFragmentListener) context;
        }

        itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onNewsItemClicked(KEY_HOLDER, newsItems.get(getAdapterPosition()).getId());
            }
        });
    }

    public void bind(NewsEntity newsItem) {
        mTitle.setText(newsItem.getTitle());
        setImage(newsItem.getImageUrl());
        setCategory(newsItem.getCategory());
        setPublishDate(newsItem.getPublishedDate());
        mPreviewText.setText(newsItem.getText());
    }

    private void findViews(View itemView){
        mTitle = itemView.findViewById(R.id.news_title);
        mImageUrl = itemView.findViewById(R.id.news_image);
        mCategory = itemView.findViewById(R.id.news_category);
        mPublishDate = itemView.findViewById(R.id.news_date);
        mPreviewText = itemView.findViewById(R.id.news_previews_text);
        mProgressBar = itemView.findViewById(R.id.pb_image);
    }

    private void setImage(String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder);
        if (url.isEmpty()){ // если нет картинок, ставим дефолтную картинку
            Glide.with(mContext)
                    .load(R.drawable.no_image)
                    .into(mImageUrl);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                            Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                            Target<Drawable> target, DataSource dataSource,
                            boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(requestOptions)
                .into(mImageUrl);
    }

    private void setCategory(String category) {
        mCategory.setVisibility(View.VISIBLE);
        if (category.isEmpty())
            mCategory.setVisibility(View.GONE);
        else
            mCategory.setText(category);
    }

    private void setPublishDate(String publishDate) {
        mPublishDate.setText(publishDate);
    }
}
