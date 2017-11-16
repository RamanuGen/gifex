package org.ramanugen.gifex.factory;

import org.ramanugen.gifex.adapter.ModelAdapter;
import org.ramanugen.gifex.constants.GifSource;
import org.ramanugen.gifex.source.gifskey.adapter.GifskeyModelAdapter;
import org.ramanugen.gifex.source.giphy.adapter.GiphyModelAdapter;

import okhttp3.OkHttpClient;

/**
 * Created by rbojja on 10/17/2017.
 */

public class AdapterFactory {
    private static OkHttpClient httpClient;
    private static GiphyModelAdapter giphyAdapter;
    private static ModelAdapter gifskeyAdapter;

    public static void init(OkHttpClient client){
        httpClient = client;
    }

    public static ModelAdapter getAdapter(GifSource source) {
        switch (source){
            case GIPHY:
                return getGiphyAdapter();
            case GIFSKEY:
                return getGifskeyAdapter();
            default:
                return getGiphyAdapter();
        }
    }

    private static GiphyModelAdapter getGiphyAdapter(){
        if(giphyAdapter == null){
            giphyAdapter = new GiphyModelAdapter(httpClient);
        }
        return giphyAdapter;
    }

    public static ModelAdapter getGifskeyAdapter() {
        if(gifskeyAdapter == null){
            gifskeyAdapter = new GifskeyModelAdapter(httpClient);
        }
        return gifskeyAdapter;
    }
}
