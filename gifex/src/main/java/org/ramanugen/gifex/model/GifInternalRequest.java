package org.ramanugen.gifex.model;

/**
 * Created by rbojja on 10/17/2017.
 */

public class GifInternalRequest extends GifRequest {
    public int offset;
    public GifInternalRequest(){

    }

    public GifInternalRequest(GifRequest request){
        this.keyword = request.keyword;
        this.eachRequestLimit = request.eachRequestLimit;
        this.maxLimit = request.maxLimit;
        this.source = request.source;
        this.apiKey = request.apiKey;
    }
}
