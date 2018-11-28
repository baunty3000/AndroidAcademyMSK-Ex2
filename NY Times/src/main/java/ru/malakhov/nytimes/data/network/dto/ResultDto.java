
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static android.text.format.DateUtils.HOUR_IN_MILLIS;

public class ResultDto {

    @SerializedName("subsection")
    @Expose
    private String mCategory;
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("abstract")
    @Expose
    private String mText;
    @SerializedName("url")
    @Expose
    private String mUrl;
    @SerializedName("published_date")
    @Expose
    private String mPublishedDate;
    @SerializedName("multimedia")
    @Expose
    private List<MultimediumDto> mMultimedia;

    public String getCategory() {
        return mCategory;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getText() {
        return mText;
    }
    public String getUrl() {
        return mUrl;
    }
    public String getPublishedDate() {
        return mPublishedDate;
    }
    public List<MultimediumDto> getMultimedia() {
        return mMultimedia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultDto resultDto = (ResultDto) o;
        return Objects.equals(mCategory, resultDto.mCategory) && Objects
                .equals(mTitle, resultDto.mTitle) && Objects.equals(mText, resultDto.mText)
                && Objects.equals(mUrl, resultDto.mUrl) && Objects
                .equals(mPublishedDate, resultDto.mPublishedDate) && Objects
                .equals(mMultimedia, resultDto.mMultimedia);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mCategory, mTitle, mText, mUrl, mPublishedDate, mMultimedia);
    }

    @Override
    public String toString() {
        return "ResultDto{" + "mCategory='" + mCategory + '\'' + ", mTitle='" + mTitle + '\''
                + ", mText='" + mText + '\'' + ", mUrl='" + mUrl + '\'' + ", mPublishedDate='"
                + mPublishedDate + '\'' + ", mMultimedia=" + mMultimedia + '}';
    }

    public static CharSequence formatDateTime(Context context, Date dateTime) {
        return DateUtils.getRelativeDateTimeString(
                context,
                dateTime.getTime(),
                HOUR_IN_MILLIS,
                5 * DAY_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE
        );
    }
}
