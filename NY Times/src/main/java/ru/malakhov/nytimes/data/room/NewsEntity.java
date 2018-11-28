package ru.malakhov.nytimes.data.room;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class NewsEntity {

    public NewsEntity() {
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String mId;

    @NonNull
    @ColumnInfo(name = "section")
    private String mSection;

    @NonNull
    @ColumnInfo(name = "subsection")
    private String mCategory;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "abstract")
    private String mText;

    @NonNull
    @ColumnInfo(name = "url")
    private String mUrl;

    @NonNull
    @ColumnInfo(name = "publisheddate")
    private String mPublishedDate;

    @NonNull
    @ColumnInfo(name = "imageurl")
    private String mImageUrl;

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getSection() {
        return mSection;
    }

    @NonNull
    public String getCategory() {
        return mCategory;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    @NonNull
    public String getUrl() {
        return mUrl;
    }

    @NonNull
    public String getPublishedDate() {
        return mPublishedDate;
    }

    @NonNull
    public String getImageUrl() {
        return mImageUrl;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public void setSection(@NonNull String section) {
        mSection = section;
    }

    public void setCategory(@NonNull String category) {
        mCategory = category;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    public void setText(@NonNull String text) {
        mText = text;
    }

    public void setUrl(@NonNull String url) {
        mUrl = url;
    }

    public void setPublishedDate(@NonNull String publishedDate) {
        mPublishedDate = publishedDate;
    }

    public void setImageUrl(@NonNull String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewsEntity that = (NewsEntity) o;
        return Objects.equals(mId, that.mId) && Objects.equals(mSection, that.mSection) && Objects
                .equals(mCategory, that.mCategory) && Objects.equals(mTitle, that.mTitle) && Objects
                .equals(mText, that.mText) && Objects.equals(mUrl, that.mUrl) && Objects
                .equals(mPublishedDate, that.mPublishedDate) && Objects
                .equals(mImageUrl, that.mImageUrl);
    }

    @Override
    public int hashCode() {

        return Objects
                .hash(mId, mSection, mCategory, mTitle, mText, mUrl, mPublishedDate, mImageUrl);
    }

    @Override
    public String toString() {
        return "NewsEntity{" + "mId='" + mId + '\'' + ", mSection='" + mSection + '\''
                + ", mCategory='" + mCategory + '\'' + ", mTitle='" + mTitle + '\'' + ", mText='"
                + mText + '\'' + ", mUrl='" + mUrl + '\'' + ", mPublishedDate='" + mPublishedDate
                + '\'' + ", mImageUrl='" + mImageUrl + '\'' + '}';
    }
}
