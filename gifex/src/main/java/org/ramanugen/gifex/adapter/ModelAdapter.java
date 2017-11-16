package org.ramanugen.gifex.adapter;

import org.ramanugen.gifex.model.GifInternalRequest;
import org.ramanugen.gifex.model.ImageObject;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by rbojja on 10/17/2017.
 */

public interface ModelAdapter {
    public Observable<ArrayList<ImageObject>> getModels(GifInternalRequest request);
}
