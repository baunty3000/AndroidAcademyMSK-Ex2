package ru.malakhov.nytimes.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class NewsItem implements Parcelable {

    private final String title;
    private final String imageUrl;
    private final Category category;
    private final Date publishDate;
    private final String previewText;
    private final String fullText;

    public NewsItem(String title, String imageUrl, Category category, Date publishDate, String previewText, String fullText) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.fullText = fullText;
    }

    protected NewsItem(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        previewText = in.readString();
        fullText = in.readString();
        publishDate = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeParcelable(category, flags);
        dest.writeString(previewText);
        dest.writeString(fullText);
        dest.writeLong(publishDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getTitle() {
        return title;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public Category getCategory() {
        return category;
    }
    public Date getPublishDate() {
        return publishDate;
    }
    public String getPreviewText() {
        return previewText;
    }
    public String getFullText() {
        return fullText;
    }
}