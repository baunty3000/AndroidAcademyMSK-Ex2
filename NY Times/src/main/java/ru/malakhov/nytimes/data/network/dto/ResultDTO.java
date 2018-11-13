
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.List;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static android.text.format.DateUtils.HOUR_IN_MILLIS;

public class ResultDTO {

    @SerializedName("subsection")
    @Expose
    private String mSubsection;
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("abstract")
    @Expose
    private String mAbstract;
    @SerializedName("url")
    @Expose
    private String mUrl;
    @SerializedName("published_date")
    @Expose
    private String mPublishedDate;
    @SerializedName("multimedia")
    @Expose
    private List<MultimediumGTO> mMultimedia;

    public String getSubsection() {
        return mSubsection;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getAbstract() {
        return mAbstract;
    }
    public String getUrl() {
        return mUrl;
    }
    public String getPublishedDate() {
        return mPublishedDate;
    }
    public List<MultimediumGTO> getMultimedia() {
        return mMultimedia;
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
