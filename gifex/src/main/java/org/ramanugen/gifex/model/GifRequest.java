package org.ramanugen.gifex.model;

import org.ramanugen.gifex.constants.GifSource;

/**
 * Created by rbojja on 10/17/2017.
 */

public class GifRequest {
    public static final int DEFAULT_MAX_LIMIT = 100;
    public static final int DEFAULT_REQUEST_LIMIT = 5;
    public static final GifSource DEFAULT_GIF_SOURCE = GifSource.GIPHY;

    public String apiKey;
    public String keyword;
    public int maxLimit;
    public int eachRequestLimit;
    public GifSource source;

    // Default constructor
    public GifRequest(){
        // Do nothing
    }

    // copy constructor
    public GifRequest(GifRequest request){
        this.apiKey = request.apiKey;
        this.keyword = request.keyword;
        this.maxLimit = (request.maxLimit > 0) ? request.maxLimit : DEFAULT_MAX_LIMIT;
        this.eachRequestLimit = (request.eachRequestLimit > 0)
                                    ? request.eachRequestLimit : DEFAULT_REQUEST_LIMIT;
        this.source = (request.source != null) ? request.source : DEFAULT_GIF_SOURCE;
    }
}
