
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataNewsDto {

    @SerializedName("results")
    @Expose
    private List<ResultDto> mResults;

    public List<ResultDto> getResults() {
        return mResults;
    }
}
