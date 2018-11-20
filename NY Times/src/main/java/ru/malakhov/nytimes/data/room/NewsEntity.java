package ru.malakhov.nytimes.data.room;

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
    private String mSubsection;

    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @NonNull
    @ColumnInfo(name = "abstract")
    private String mAbstract;

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
    public String getSubsection() {
        return mSubsection;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getAbstract() {
        return mAbstract;
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

    public void setSubsection(@NonNull String subsection) {
        mSubsection = subsection;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    public void setAbstract(@NonNull String anAbstract) {
        mAbstract = anAbstract;
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
}
