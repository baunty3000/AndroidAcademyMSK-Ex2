
package ru.malakhov.nytimes.data.network.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataNewsDTO {

    @SerializedName("results")
    @Expose
    private List<ResultDTO> mResults;

    public List<ResultDTO> getResults() {
        return mResults;
    }
}
