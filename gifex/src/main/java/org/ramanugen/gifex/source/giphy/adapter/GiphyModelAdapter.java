package org.ramanugen.gifex.source.giphy.adapter;

import org.ramanugen.gifex.adapter.ModelAdapter;
import org.ramanugen.gifex.model.GifInternalRequest;
import org.ramanugen.gifex.model.ImageObject;
import org.ramanugen.gifex.source.giphy.constants.GiphyConstants;
import org.ramanugen.gifex.source.giphy.model.GiphyImage;
import org.ramanugen.gifex.source.giphy.model.GiphyImageDetails;
import org.ramanugen.gifex.source.giphy.model.GiphyImageVariations;
import org.ramanugen.gifex.source.giphy.model.GiphyResponse;
import org.ramanugen.gifex.source.giphy.transport.GiphyService;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by rbojja on 10/17/2017.
 */

public class GiphyModelAdapter implements ModelAdapter {
    private GiphyService giphyService;
    private static Scheduler giphyScheduler;

    public GiphyModelAdapter(OkHttpClient client){
        giphyScheduler = Schedulers.from(Executors.newFixedThreadPool(8));
        giphyService = GiphyService.Creator.newService(client, GiphyConstants.GIPHY_BASE_URL,
                giphyScheduler);
    }

    @Override
    public Observable<ArrayList<ImageObject>> getModels(GifInternalRequest request) {
        return getGiphiesAsGifs(
                request.keyword,request.offset,request.eachRequestLimit,request.apiKey)
                .map(new Func1<ArrayList<GiphyImage>, ArrayList<ImageObject>>() {
                    @Override
                    public ArrayList<ImageObject> call(ArrayList<GiphyImage> images) {
                        return convertToCommonModels(images);
                    }
                });
    }

    public Observable<ArrayList<GiphyImage>> getGiphiesAsGifs(
            final String keyword, final int offset, final int limit, final String apiKey) {
        return giphyService
                .getGiphies(keyword,limit,offset,apiKey)
                .subscribeOn(giphyScheduler)
                .map(new Func1<GiphyResponse, ArrayList<GiphyImage>>() {
                    @Override
                    public ArrayList<GiphyImage> call(GiphyResponse giphyResponse) {
                        return giphyResponse.getListOfImages();
                    }
                });
    }

    private ArrayList<ImageObject> convertToCommonModels(ArrayList<GiphyImage> images){
        ArrayList<ImageObject> models = new ArrayList<>();

        if((images != null) && !images.isEmpty()){
            for(GiphyImage giphyImage : images){
                GiphyImageVariations variations = giphyImage.getImageVariations();
                if(variations != null){
                    GiphyImageDetails details = variations.getImageDetails();
                    if(details != null){
                        ImageObject model = new ImageObject();
                        model.setUrl(details.getUrl());
                        model.setWidth(details.getWidth());
                        model.setHeight(details.getHeight());
                        model.setContentSize(details.getSize());
                        models.add(model);
                    }
                }
            }
        }

        return models;
    }
}
