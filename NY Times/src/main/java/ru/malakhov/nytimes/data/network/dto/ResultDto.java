
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

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
    private List<MultimediumDto> mImage;

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
    public List<MultimediumDto> getImage() {
        return mImage;
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
                .equals(mImage, resultDto.mImage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mCategory, mTitle, mText, mUrl, mPublishedDate, mImage);
    }

    @Override
    public String toString() {
        return "ResultDto{" + "mCategory='" + mCategory + '\'' + ", mTitle='" + mTitle + '\''
                + ", mText='" + mText + '\'' + ", mUrl='" + mUrl + '\'' + ", mPublishedDate='"
                + mPublishedDate + '\'' + ", mImage=" + mImage + '}';
    }
}
