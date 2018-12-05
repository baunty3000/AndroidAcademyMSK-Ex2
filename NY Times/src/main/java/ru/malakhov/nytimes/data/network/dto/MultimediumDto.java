
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.util.Objects;

public class MultimediumDto {

    @SerializedName("url")
    @Expose
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultimediumDto that = (MultimediumDto) o;
        return Objects.equals(mUrl, that.mUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mUrl);
    }

    @Override
    public String toString() {
        return "MultimediumDto{" + "mUrl='" + mUrl + '\'' + '}';
    }
}
