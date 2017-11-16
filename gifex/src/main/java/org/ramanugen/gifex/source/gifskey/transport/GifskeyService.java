package org.ramanugen.gifex.source.gifskey.transport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ramanugen.gifex.source.gifskey.model.GifskeyResponse;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
public interface GifskeyService {


    @GET("/v1/gifs/search")
    Observable<GifskeyResponse> getGifskies(@Query("q") String keyword,
                                            @Query("limit") int limit,
                                            @Query("offset") int offset,
                                            @Header("api_key") String apiKey);

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {

        public static GifskeyService newService(OkHttpClient client, String baseUrl, Scheduler scheduler) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(GifskeyResponse.class,new GifskeyResponseDeserializer())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(
                            scheduler != null ? scheduler : Schedulers.computation()))
                    .build();
            return retrofit.create(GifskeyService.class);
        }

    }
}
