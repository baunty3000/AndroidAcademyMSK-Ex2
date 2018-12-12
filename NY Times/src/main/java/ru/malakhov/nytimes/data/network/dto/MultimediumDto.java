
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class MultimediumDto {

    @SerializedName("url")
    @Expose
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }
}
