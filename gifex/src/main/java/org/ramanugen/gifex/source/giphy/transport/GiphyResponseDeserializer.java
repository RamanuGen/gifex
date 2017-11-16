package org.ramanugen.gifex.source.giphy.transport;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.ramanugen.gifex.source.giphy.model.GiphyImage;
import org.ramanugen.gifex.source.giphy.model.GiphyResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

;

/**
 * Created by Tapasya on 6/18/16.
 */
public class GiphyResponseDeserializer implements JsonDeserializer<GiphyResponse> {
    @Override
    public GiphyResponse deserialize(JsonElement json,
                                         Type typeOfT,
                                         JsonDeserializationContext context)
            throws JsonParseException {

        GiphyResponse response = new GiphyResponse();
        JsonObject responseObject = json.getAsJsonObject();

        JsonArray giphiesArray = responseObject.getAsJsonArray("data");
        if((giphiesArray != null) && (giphiesArray.size() > 0)){
            ArrayList<GiphyImage> giphiesList = new ArrayList<>(giphiesArray.size());
            for (JsonElement element : giphiesArray ) {
                GiphyImage giphy =  context.deserialize(element, GiphyImage.class);
                giphiesList.add(giphy);
            }
            response.setListOfImages(giphiesList);
        }
        return response;
    }
}
