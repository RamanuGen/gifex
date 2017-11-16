package org.ramanugen.gifex.controller;

import org.ramanugen.gifex.adapter.ModelAdapter;
import org.ramanugen.gifex.factory.AdapterFactory;
import org.ramanugen.gifex.model.GifInternalRequest;
import org.ramanugen.gifex.model.ImageObject;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by rbojja on 9/27/2017.
 */

public class GifFetcher {

    public static Observable<ArrayList<ImageObject>> getGifs(GifInternalRequest request) {
        ModelAdapter adapter = AdapterFactory.getAdapter(request.source);
        return adapter.getModels(request);
    }

}
