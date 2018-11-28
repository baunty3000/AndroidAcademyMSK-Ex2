
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class DataNewsDto {

    @SerializedName("results")
    @Expose
    private List<ResultDto> mResults;

    public List<ResultDto> getResults() {
        return mResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataNewsDto that = (DataNewsDto) o;
        return Objects.equals(mResults, that.mResults);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mResults);
    }

    @Override
    public String toString() {
        return "DataNewsDto{" + "mResults=" + mResults + '}';
    }
}
