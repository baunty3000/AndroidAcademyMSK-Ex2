
package ru.malakhov.nytimes.ui.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import android.app.Activity;
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
import ru.malakhov.nytimes.data.network.dto.MultimediumGTO;
import ru.malakhov.nytimes.data.network.dto.ResultDTO;
import ru.malakhov.nytimes.ui.ActivityFullNews;

public class ViewHolderNews extends RecyclerView.ViewHolder {

    private final int mImageSize = 1;

    private TextView mTitle;
    private ImageView mImageUrl;
    private TextView mCategory;
    private TextView mPublishDate;
    private TextView mPreviewText;
    private ProgressBar mProgressBar;
    private Activity mActivity;

    public ViewHolderNews(@NonNull View itemView, Activity activity, List<ResultDTO> newsItems) {
        super(itemView);
        findViews(itemView);
        mActivity = activity;
        itemView.setOnClickListener(view -> ActivityFullNews.start(mActivity, newsItems.get(getAdapterPosition()))); // тут корректно?
    }

    public void bind(ResultDTO newsItem) {
        mTitle.setText(newsItem.getTitle());
        setImage(newsItem.getMultimedia());
        setCategory(newsItem.getSubsection());
        setPublishDate(newsItem.getPublishedDate());
        mPreviewText.setText(newsItem.getAbstract());
    }

    private void findViews(View itemView){
        mTitle = itemView.findViewById(R.id.news_title);
        mImageUrl = itemView.findViewById(R.id.news_image);
        mCategory = itemView.findViewById(R.id.news_category);
        mPublishDate = itemView.findViewById(R.id.news_date);
        mPreviewText = itemView.findViewById(R.id.news_previews_text);
        mProgressBar = itemView.findViewById(R.id.pb_image);
    }

    private void setImage(List<MultimediumGTO> url) {
        if (url.size() == 0){ // если нет картинок, ставим дефолтную картинку
            Glide.with(mActivity)
                    .load(R.drawable.no_image)
                    .into(mImageUrl);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        Glide.with(mActivity)
                .load(url.get(mImageSize).getUrl())
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
                .into(mImageUrl);
    }

    private void setCategory(String category) {
        mCategory.setVisibility(View.VISIBLE);
        if (category.equals(""))
            mCategory.setVisibility(View.GONE);
        else
            mCategory.setText(category);
    }

    private void setPublishDate(String publishDate) {
        mPublishDate.setText(publishDate);
    }
}
