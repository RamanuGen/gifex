package org.ramanugen.gifex.source.giphy.transport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ramanugen.gifex.source.giphy.model.GiphyResponse;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Tapasya on 1/18/16.
 */
public interface GiphyService {


    @GET("/v1/gifs/search")
    Observable<GiphyResponse> getGiphies(@Query("q") String keyword,
                                         @Query("limit") int limit,
                                         @Query("offset") int offset,
                                         @Query("api_key") String apiKey);

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static GiphyService newService(OkHttpClient client, String baseUrl, Scheduler scheduler) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(GiphyResponse.class,new GiphyResponseDeserializer())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(
                            scheduler != null ? scheduler : Schedulers.computation()))
                    .build();
            return retrofit.create(GiphyService.class);
        }

    }
}
