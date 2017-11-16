package org.ramanugen.gifex.source.gifskey.adapter;

import org.ramanugen.gifex.adapter.ModelAdapter;
import org.ramanugen.gifex.model.GifInternalRequest;
import org.ramanugen.gifex.model.ImageObject;
import org.ramanugen.gifex.source.gifskey.constants.GifskeyConstants;
import org.ramanugen.gifex.source.gifskey.model.GifskeyImage;
import org.ramanugen.gifex.source.gifskey.model.GifskeyImageDetails;
import org.ramanugen.gifex.source.gifskey.model.GifskeyImageVariations;
import org.ramanugen.gifex.source.gifskey.model.GifskeyResponse;
import org.ramanugen.gifex.source.gifskey.transport.GifskeyService;

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

public class GifskeyModelAdapter implements ModelAdapter {
    private GifskeyService gifskeyService;
    private static Scheduler giphyScheduler;

    public GifskeyModelAdapter(OkHttpClient client){
        giphyScheduler = Schedulers.from(Executors.newFixedThreadPool(8));
        gifskeyService = GifskeyService.Creator.newService(client, GifskeyConstants.GIFSKEY_BASE_URL,
                giphyScheduler);
    }

    @Override
    public Observable<ArrayList<ImageObject>> getModels(GifInternalRequest request) {
        return getGifskiesAsGifs(
                request.keyword,request.offset,request.eachRequestLimit,request.apiKey)
                .map(new Func1<ArrayList<GifskeyImage>, ArrayList<ImageObject>>() {
                    @Override
                    public ArrayList<ImageObject> call(ArrayList<GifskeyImage> images) {
                        return convertToCommonModels(images);
                    }
                });
    }

    public Observable<ArrayList<GifskeyImage>> getGifskiesAsGifs(
            final String keyword, final int offset, final int limit, final String apiKey) {
        return gifskeyService
                .getGifskies(keyword,limit,offset,apiKey)
                .subscribeOn(giphyScheduler)
                .map(new Func1<GifskeyResponse, ArrayList<GifskeyImage>>() {
                    @Override
                    public ArrayList<GifskeyImage> call(GifskeyResponse gifskeyResponse) {
                        return gifskeyResponse.getListOfImages();
                    }
                });
    }

    private ArrayList<ImageObject> convertToCommonModels(ArrayList<GifskeyImage> images){
        ArrayList<ImageObject> models = new ArrayList<>();

        if((images != null) && !images.isEmpty()){
            for(GifskeyImage gifskeyImage : images){
                GifskeyImageVariations variations = gifskeyImage.getImageVariations();
                if(variations != null){
                    GifskeyImageDetails details = variations.getImageDetails();
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
