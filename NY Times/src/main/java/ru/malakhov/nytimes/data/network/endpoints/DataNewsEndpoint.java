
package ru.malakhov.nytimes.data.network.endpoints;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.malakhov.nytimes.data.network.dto.DataNewsDTO;

public interface DataNewsEndpoint {
    @GET("/svc/topstories/v2/{section}.json")
    Single<DataNewsDTO> getNews(
            @Path("section") String section);
}
