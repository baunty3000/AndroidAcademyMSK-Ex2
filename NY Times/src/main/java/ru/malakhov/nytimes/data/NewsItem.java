package ru.malakhov.nytimes.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import java.util.Objects;

public class NewsItem implements Parcelable {

    private final String mTitle;
    private final String mImageUrl;
    private final Category mCategory;
    private final Date mPublishDate;
    private final String mPreviewText;
    private final String mFullText;

    public NewsItem(String title, String imageUrl, Category category, Date publishDate,
            String previewText, String fullText) {
        mTitle = title;
        mImageUrl = imageUrl;
        mCategory = category;
        mPublishDate = publishDate;
        mPreviewText = previewText;
        mFullText = fullText;
    }

    protected NewsItem(Parcel in) {
        mTitle = in.readString();
        mImageUrl = in.readString();
        mCategory = in.readParcelable(Category.class.getClassLoader());
        mPreviewText = in.readString();
        mFullText = in.readString();
        mPublishDate = new Date(in.readLong());
    }

    public String getTitle() {
        return mTitle;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public Category getCategory() {
        return mCategory;
    }
    public Date getPublishDate() {
        return mPublishDate;
    }
    public String getPreviewText() {
        return mPreviewText;
    }
    public String getFullText() {
        return mFullText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewsItem newsItem = (NewsItem) o;
        return Objects.equals(mTitle, newsItem.mTitle) && Objects
                .equals(mImageUrl, newsItem.mImageUrl) && Objects
                .equals(mCategory, newsItem.mCategory) && Objects
                .equals(mPublishDate, newsItem.mPublishDate) && Objects
                .equals(mPreviewText, newsItem.mPreviewText) && Objects
                .equals(mFullText, newsItem.mFullText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mTitle, mImageUrl, mCategory, mPublishDate, mPreviewText, mFullText);
    }

    @Override
    public String toString() {
        return "NewsItem{"
                + "mTitle='" + mTitle + '\''
                + ", mImageUrl='" + mImageUrl + '\''
                + ", mCategory=" + mCategory
                + ", mPublishDate=" + mPublishDate
                + ", mPreviewText='" + mPreviewText + '\''
                + ", mFullText='" + mFullText + '\''
                + '}';
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mImageUrl);
        dest.writeParcelable(mCategory, flags);
        dest.writeString(mPreviewText);
        dest.writeString(mFullText);
        dest.writeLong(mPublishDate.getTime());
    }
}
